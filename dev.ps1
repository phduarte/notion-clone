# Notion Clone - Development Helper Script (Windows)

$ErrorActionPreference = "Stop"

Write-Host "=== Notion Clone Development Helper ===" -ForegroundColor Green
Write-Host ""

# Function to check if command exists
function Test-Command {
    param($Command)
    $null -ne (Get-Command $Command -ErrorAction SilentlyContinue)
}

# Check prerequisites
Write-Host "Checking prerequisites..."

if (-not (Test-Command java)) {
    Write-Host "❌ Java not found. Please install JDK 17+" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Java found" -ForegroundColor Green

if (-not (Test-Command node)) {
    Write-Host "❌ Node.js not found. Please install Node.js 18+" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Node.js found: $(node --version)" -ForegroundColor Green

if (-not (Test-Command pnpm)) {
    Write-Host "⚠ pnpm not found. Installing..." -ForegroundColor Yellow
    npm install -g pnpm
}
Write-Host "✓ pnpm found: $(pnpm --version)" -ForegroundColor Green

if (-not (Test-Command psql)) {
    Write-Host "⚠ PostgreSQL psql not found. Make sure PostgreSQL is installed and in PATH" -ForegroundColor Yellow
}

Write-Host ""

# Menu
Write-Host "What would you like to do?"
Write-Host "1) Setup project (first time)"
Write-Host "2) Start backend"
Write-Host "3) Start frontend"
Write-Host "4) Start both (backend + frontend)"
Write-Host "5) Run tests"
Write-Host "6) Clean build"
Write-Host "7) Docker compose up"
Write-Host "8) Docker compose down"
Write-Host "9) Reset database"
Write-Host "0) Exit"
Write-Host ""

$choice = Read-Host "Enter your choice"

switch ($choice) {
    "1" {
        Write-Host "`nSetting up project..." -ForegroundColor Green
        Write-Host ""
        
        # Create database
        Write-Host "Creating database..."
        try {
            & psql -U postgres -c "CREATE DATABASE notionclone_dev;"
        } catch {
            Write-Host "Database might already exist" -ForegroundColor Yellow
        }
        
        # Backend setup
        Write-Host "`nSetting up backend..." -ForegroundColor Yellow
        Set-Location backend
        
        if (-not (Test-Path "src\main\resources\application-local.yml")) {
            Write-Host "Creating application-local.yml..."
            @"
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/notionclone_dev
    username: postgres
    password: postgres
  
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

app:
  jwt:
    secret: change-this-to-a-strong-secret-with-at-least-32-characters
    expiration: 900000
    refresh-expiration: 604800000
"@ | Out-File -FilePath "src\main\resources\application-local.yml" -Encoding UTF8
            Write-Host "⚠ Please edit backend\src\main\resources\application-local.yml with your settings" -ForegroundColor Yellow
        }
        
        .\gradlew.bat build -x test
        
        # Frontend setup
        Write-Host "`nSetting up frontend..." -ForegroundColor Yellow
        Set-Location ..\frontend
        
        if (-not (Test-Path ".env.local")) {
            Write-Host "Creating .env.local..."
            "NEXT_PUBLIC_API_URL=http://localhost:8080/api" | Out-File -FilePath ".env.local" -Encoding UTF8
        }
        
        pnpm install
        
        Set-Location ..
        Write-Host "`n✓ Setup complete!" -ForegroundColor Green
        Write-Host "Remember to update application-local.yml with your email settings" -ForegroundColor Yellow
    }
    
    "2" {
        Write-Host "`nStarting backend..." -ForegroundColor Green
        Write-Host ""
        Set-Location backend
        .\gradlew.bat bootRun --args='--spring.profiles.active=local'
    }
    
    "3" {
        Write-Host "`nStarting frontend..." -ForegroundColor Green
        Write-Host ""
        Set-Location frontend
        pnpm dev
    }
    
    "4" {
        Write-Host "`nStarting both backend and frontend..." -ForegroundColor Green
        Write-Host ""
        
        $backend = Start-Process -FilePath "powershell" -ArgumentList "-Command", "cd backend; .\gradlew.bat bootRun --args='--spring.profiles.active=local'" -PassThru
        Start-Sleep -Seconds 2
        $frontend = Start-Process -FilePath "powershell" -ArgumentList "-Command", "cd frontend; pnpm dev" -PassThru
        
        Write-Host "Press any key to stop servers..."
        $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
        
        Stop-Process -Id $backend.Id -Force
        Stop-Process -Id $frontend.Id -Force
    }
    
    "5" {
        Write-Host "`nRunning tests..." -ForegroundColor Green
        Write-Host ""
        
        Write-Host "Backend tests..."
        Set-Location backend
        .\gradlew.bat test
        
        Write-Host "`nFrontend tests..."
        Set-Location ..\frontend
        pnpm test
        
        Set-Location ..
        Write-Host "`n✓ All tests complete" -ForegroundColor Green
    }
    
    "6" {
        Write-Host "`nCleaning build..." -ForegroundColor Green
        Write-Host ""
        
        Set-Location backend
        .\gradlew.bat clean
        
        Set-Location ..\frontend
        Remove-Item -Recurse -Force .next -ErrorAction SilentlyContinue
        Remove-Item -Recurse -Force node_modules\.cache -ErrorAction SilentlyContinue
        
        Set-Location ..
        Write-Host "✓ Clean complete" -ForegroundColor Green
    }
    
    "7" {
        Write-Host "`nStarting Docker containers..." -ForegroundColor Green
        Write-Host ""
        docker-compose up -d
        Write-Host "✓ Containers started" -ForegroundColor Green
        Write-Host "Frontend: http://localhost:3000"
        Write-Host "Backend: http://localhost:8080"
        Write-Host "Swagger: http://localhost:8080/swagger-ui.html"
    }
    
    "8" {
        Write-Host "`nStopping Docker containers..." -ForegroundColor Green
        Write-Host ""
        docker-compose down
        Write-Host "✓ Containers stopped" -ForegroundColor Green
    }
    
    "9" {
        Write-Host "`n⚠ WARNING: This will delete all data!" -ForegroundColor Red
        $confirm = Read-Host "Are you sure? (yes/no)"
        if ($confirm -eq "yes") {
            Write-Host "Dropping database..."
            & psql -U postgres -c "DROP DATABASE IF EXISTS notionclone_dev;"
            Write-Host "Creating database..."
            & psql -U postgres -c "CREATE DATABASE notionclone_dev;"
            Write-Host "✓ Database reset complete" -ForegroundColor Green
        } else {
            Write-Host "Cancelled"
        }
    }
    
    "0" {
        Write-Host "Goodbye!"
        exit 0
    }
    
    default {
        Write-Host "Invalid choice" -ForegroundColor Red
        exit 1
    }
}

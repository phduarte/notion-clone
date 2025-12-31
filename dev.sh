#!/bin/bash

# Notion Clone - Development Helper Script

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== Notion Clone Development Helper ===${NC}\n"

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check prerequisites
echo "Checking prerequisites..."

if ! command_exists java; then
    echo -e "${RED}❌ Java not found. Please install JDK 17+${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Java found: $(java -version 2>&1 | head -n 1)${NC}"

if ! command_exists node; then
    echo -e "${RED}❌ Node.js not found. Please install Node.js 18+${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Node.js found: $(node --version)${NC}"

if ! command_exists pnpm; then
    echo -e "${YELLOW}⚠ pnpm not found. Installing...${NC}"
    npm install -g pnpm
fi
echo -e "${GREEN}✓ pnpm found: $(pnpm --version)${NC}"

if ! command_exists psql; then
    echo -e "${RED}❌ PostgreSQL not found. Please install PostgreSQL 15+${NC}"
    exit 1
fi
echo -e "${GREEN}✓ PostgreSQL found${NC}"

echo ""

# Menu
echo "What would you like to do?"
echo "1) Setup project (first time)"
echo "2) Start backend"
echo "3) Start frontend"
echo "4) Start both (backend + frontend)"
echo "5) Run tests"
echo "6) Clean build"
echo "7) Docker compose up"
echo "8) Docker compose down"
echo "9) Reset database"
echo "0) Exit"
echo ""
read -p "Enter your choice: " choice

case $choice in
    1)
        echo -e "\n${GREEN}Setting up project...${NC}\n"
        
        # Create database
        echo "Creating database..."
        createdb notionclone_dev 2>/dev/null || echo "Database already exists"
        
        # Backend setup
        echo -e "\n${YELLOW}Setting up backend...${NC}"
        cd backend
        
        if [ ! -f "src/main/resources/application-local.yml" ]; then
            echo "Creating application-local.yml..."
            cat > src/main/resources/application-local.yml << EOF
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
EOF
            echo -e "${YELLOW}⚠ Please edit backend/src/main/resources/application-local.yml with your settings${NC}"
        fi
        
        ./gradlew build -x test
        
        # Frontend setup
        echo -e "\n${YELLOW}Setting up frontend...${NC}"
        cd ../frontend
        
        if [ ! -f ".env.local" ]; then
            echo "Creating .env.local..."
            echo "NEXT_PUBLIC_API_URL=http://localhost:8080/api" > .env.local
        fi
        
        pnpm install
        
        echo -e "\n${GREEN}✓ Setup complete!${NC}"
        echo -e "${YELLOW}Remember to update application-local.yml with your email settings${NC}"
        ;;
        
    2)
        echo -e "\n${GREEN}Starting backend...${NC}\n"
        cd backend
        ./gradlew bootRun --args='--spring.profiles.active=local'
        ;;
        
    3)
        echo -e "\n${GREEN}Starting frontend...${NC}\n"
        cd frontend
        pnpm dev
        ;;
        
    4)
        echo -e "\n${GREEN}Starting both backend and frontend...${NC}\n"
        trap 'kill 0' EXIT
        
        cd backend
        ./gradlew bootRun --args='--spring.profiles.active=local' &
        
        cd ../frontend
        pnpm dev &
        
        wait
        ;;
        
    5)
        echo -e "\n${GREEN}Running tests...${NC}\n"
        
        echo "Backend tests..."
        cd backend
        ./gradlew test
        
        echo -e "\nFrontend tests..."
        cd ../frontend
        pnpm test
        
        echo -e "\n${GREEN}✓ All tests complete${NC}"
        ;;
        
    6)
        echo -e "\n${GREEN}Cleaning build...${NC}\n"
        
        cd backend
        ./gradlew clean
        
        cd ../frontend
        rm -rf .next node_modules/.cache
        
        echo -e "${GREEN}✓ Clean complete${NC}"
        ;;
        
    7)
        echo -e "\n${GREEN}Starting Docker containers...${NC}\n"
        docker-compose up -d
        echo -e "${GREEN}✓ Containers started${NC}"
        echo "Frontend: http://localhost:3000"
        echo "Backend: http://localhost:8080"
        echo "Swagger: http://localhost:8080/swagger-ui.html"
        ;;
        
    8)
        echo -e "\n${GREEN}Stopping Docker containers...${NC}\n"
        docker-compose down
        echo -e "${GREEN}✓ Containers stopped${NC}"
        ;;
        
    9)
        echo -e "\n${RED}⚠ WARNING: This will delete all data!${NC}"
        read -p "Are you sure? (yes/no): " confirm
        if [ "$confirm" = "yes" ]; then
            echo "Dropping database..."
            dropdb notionclone_dev 2>/dev/null || true
            echo "Creating database..."
            createdb notionclone_dev
            echo -e "${GREEN}✓ Database reset complete${NC}"
        else
            echo "Cancelled"
        fi
        ;;
        
    0)
        echo "Goodbye!"
        exit 0
        ;;
        
    *)
        echo -e "${RED}Invalid choice${NC}"
        exit 1
        ;;
esac

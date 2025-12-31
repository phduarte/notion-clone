export interface User {
  id: string
  name: string
  username: string
  email: string
  phone?: string
  plan: 'FREE' | 'PRO' | 'TEAM' | 'ENTERPRISE'
  avatar?: string
  emailVerified: boolean
  firstLogin: boolean
  createdAt: string
}

export interface AuthResponse {
  user: User
  tokens: {
    accessToken: string
    refreshToken: string
    type: string
    expiresIn: number
  }
}

export interface RegisterDto {
  name: string
  username: string
  email: string
  phone?: string
  password: string
  plan: 'FREE' | 'PRO' | 'TEAM' | 'ENTERPRISE'
}

export interface LoginDto {
  email: string
  password: string
}

export interface Document {
  id: string
  title: string
  content?: string
  icon?: string
  cover?: string
  ownerId: string
  ownerName: string
  parentId?: string
  isFavorite: boolean
  isArchived: boolean
  isPublic: boolean
  publicSlug?: string
  allowComments: boolean
  subPagesCount: number
  createdAt: string
  updatedAt: string
  lastEditedByName?: string
}

export interface DocumentSummary {
  id: string
  title: string
  icon?: string
  isFavorite: boolean
  isArchived: boolean
  subPagesCount: number
  updatedAt: string
}

export interface CreateDocumentDto {
  title: string
  content?: string
  icon?: string
  cover?: string
  parentId?: string
}

export interface UpdateDocumentDto {
  title?: string
  content?: string
  icon?: string
  cover?: string
  isFavorite?: boolean
  isArchived?: boolean
}

import api from '@/lib/api'
import type {
  Document,
  DocumentSummary,
  CreateDocumentDto,
  UpdateDocumentDto,
} from '@/types'

export const documentService = {
  async create(data: CreateDocumentDto): Promise<Document> {
    const response = await api.post<Document>('/documents', data)
    return response.data
  },

  async getById(id: string): Promise<Document> {
    const response = await api.get<Document>(`/documents/${id}`)
    return response.data
  },

  async update(id: string, data: UpdateDocumentDto): Promise<Document> {
    const response = await api.patch<Document>(`/documents/${id}`, data)
    return response.data
  },

  async delete(id: string): Promise<void> {
    await api.delete(`/documents/${id}`)
  },

  async getMyDocuments(): Promise<DocumentSummary[]> {
    const response = await api.get<DocumentSummary[]>('/documents')
    return response.data
  },

  async getSubPages(parentId: string): Promise<DocumentSummary[]> {
    const response = await api.get<DocumentSummary[]>(
      `/documents/${parentId}/sub-pages`
    )
    return response.data
  },

  async getFavorites(): Promise<DocumentSummary[]> {
    const response = await api.get<DocumentSummary[]>('/documents/favorites')
    return response.data
  },

  async getArchived(): Promise<DocumentSummary[]> {
    const response = await api.get<DocumentSummary[]>('/documents/archived')
    return response.data
  },

  async search(query: string): Promise<DocumentSummary[]> {
    const response = await api.get<DocumentSummary[]>('/documents/search', {
      params: { q: query },
    })
    return response.data
  },

  async makePublic(id: string, slug: string, isPublic: boolean): Promise<Document> {
    const response = await api.patch<Document>(`/documents/${id}/public`, {
      slug,
      isPublic,
    })
    return response.data
  },

  async getBySlug(slug: string): Promise<Document> {
    const response = await api.get<Document>(`/public/documents/${slug}`)
    return response.data
  },
}

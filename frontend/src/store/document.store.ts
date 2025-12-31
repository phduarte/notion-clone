import { create } from 'zustand'
import type { DocumentSummary } from '@/types'

interface DocumentState {
  documents: DocumentSummary[]
  activeDocumentId: string | null
  setDocuments: (documents: DocumentSummary[]) => void
  setActiveDocumentId: (id: string | null) => void
  addDocument: (document: DocumentSummary) => void
  updateDocument: (id: string, updates: Partial<DocumentSummary>) => void
  removeDocument: (id: string) => void
}

export const useDocumentStore = create<DocumentState>((set) => ({
  documents: [],
  activeDocumentId: null,
  setDocuments: (documents) => set({ documents }),
  setActiveDocumentId: (id) => set({ activeDocumentId: id }),
  addDocument: (document) =>
    set((state) => ({ documents: [...state.documents, document] })),
  updateDocument: (id, updates) =>
    set((state) => ({
      documents: state.documents.map((doc) =>
        doc.id === id ? { ...doc, ...updates } : doc
      ),
    })),
  removeDocument: (id) =>
    set((state) => ({
      documents: state.documents.filter((doc) => doc.id !== id),
    })),
}))

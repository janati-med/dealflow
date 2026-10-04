export const STAGES = ['LEAD', 'ANGEBOT', 'VERHANDLUNG', 'GEWONNEN', 'VERLOREN'] as const
export type Stage = (typeof STAGES)[number]

export interface Customer {
    id: number
    name: string
    company: string | null
    email: string | null
}

export interface Deal {
    id: number
    title: string
    value: number
    stage: Stage
    customerId: number
    owner: string | null
}

export interface TokenResponse {
    token: string
    role: string
    expiresInSeconds: number
}
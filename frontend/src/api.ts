import type { Customer, Deal, Stage, TokenResponse } from './types'

const TOKEN_KEY = 'dealflow_token'

export const getToken = () => localStorage.getItem(TOKEN_KEY)
export const setToken = (token: string | null) => {
    if (token) localStorage.setItem(TOKEN_KEY, token)
    else localStorage.removeItem(TOKEN_KEY)
}

export class ApiError extends Error {
    status: number
    constructor(status: number, message: string) {
        super(message)
        this.status = status
    }
}

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
    const token = getToken()
    const response = await fetch(path, {
        ...options,
        headers: {
            'Content-Type': 'application/json',
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
    })
    if (!response.ok) throw new ApiError(response.status, response.statusText)
    if (response.status === 204) return undefined as T
    return response.json()
}

export const api = {
    login: (username: string, password: string) =>
        request<TokenResponse>('/api/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password }),
        }),
    customers: () => request<Customer[]>('/api/customers'),
    deals: () => request<Deal[]>('/api/deals'),
    createDeal: (body: { title: string; value: number; customerId: number }) =>
        request<Deal>('/api/deals', { method: 'POST', body: JSON.stringify(body) }),
    moveDeal: (id: number, stage: Stage) =>
        request<Deal>(`/api/deals/${id}/stage?stage=${stage}`, { method: 'PATCH' }),
}
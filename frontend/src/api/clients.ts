import type { Client } from "../types/clinet";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL

export async function fetchClients(): Promise<Client[]> {
    const response = await fetch(`${apiBaseUrl}/clients`)

    if (!response.ok) {
        throw new Error(
            `client一覧の取得に失敗しました。status=${response.status}`,
        )
    }

    return (await response.json()) as Client[]
}
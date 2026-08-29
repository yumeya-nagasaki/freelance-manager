import { useQuery } from "@tanstack/react-query";
import { fetchClients } from "../api/clients";

export function ClientListPage() {
    const {
        data: clients,
        isPending,
        isError,
        error,
    } = useQuery({
        queryKey: ["clients"],
        queryFn: fetchClients,
    })

    if (isPending) {
        return <p>取引先を読み込んでいます...</p>
    }

    if (isError) {
        return (
            <section>
                <h2>エラーが発生しました</h2>
                <p>{error.message}</p>
            </section>
        )
    }

    return (
        <main>
            <h1>取引先一覧</h1>

            {clients.length === 0 ? (
                <p>登録されている取引先はありません。</p>
            ) : (
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>取引先名</th>
                            <th>メールアドレス</th>
                            <th>備考</th>
                        </tr>
                    </thead>
                    <tbody>
                        {clients.map((client) => (
                            <tr key={client.id}>
                                <td>{client.id}</td>
                                <td>{client.name}</td>
                                <td>{client.email ?? '-'}</td>
                                <td>{client.memo ?? '-'}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </main>
    )
}
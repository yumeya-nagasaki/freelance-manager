import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {createRoot} from "react-dom/client";
import {StrictMode} from "react";
import {BrowserRouter} from "react-router";
import App from "./App.tsx";
import {ReactQueryDevtools} from "@tanstack/react-query-devtools";

import './index.css'

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            retry: 1,
            staleTime: 30_000,
        },
    },
})

const rootElement = document.getElementById('root')

if (!rootElement) {
    throw new Error('root要素が見つかりません')
}

createRoot(rootElement).render(
    <StrictMode>
        <BrowserRouter>
            <QueryClientProvider client={queryClient}>
                <App/>
                <ReactQueryDevtools initialIsOpen={false}/>
            </QueryClientProvider>
        </BrowserRouter>
    </StrictMode>
)
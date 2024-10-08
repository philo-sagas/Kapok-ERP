import '@/styles/globals.css'
import '@/styles/Material+Icons.css'
import type {Metadata} from 'next'
import {Inter} from 'next/font/google'

const inter = Inter({subsets: ['latin']})

export const metadata: Metadata = {
  title: 'Kapok-ERP'
}

export default function RootLayout({children}: {
  children: React.ReactNode
}) {
  return (
    <html lang="zh-CN">
    <body className={inter.className}>{children}</body>
    </html>
  )
}

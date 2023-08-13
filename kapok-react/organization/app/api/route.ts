import {NextResponse} from 'next/server'
import {cookies, headers} from 'next/headers'

export async function GET(request: Request) {
  const {searchParams} = new URL(request.url)
  const code = searchParams.get('code') || ''
  const state = searchParams.get('state') || ''

  return new Response('Hello, Api!', {
    status: 200,
    headers: {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
      'Access-Control-Allow-Headers': 'Content-Type, Authorization',
      'X-Oauth2-Code': code,
      'X-Oauth2-State': state
    },
  })
}

export async function POST(request: Request) {
  const cookieStore = cookies()
  const headersList = headers()
  const session = cookieStore.get('SESSION')
  const jsessionid = cookieStore.get('JSESSIONID')
  const referer = headersList.get('referer')
  const data = await request.json()

  return NextResponse.json({
    session,
    jsessionid,
    referer,
    data
  })
}

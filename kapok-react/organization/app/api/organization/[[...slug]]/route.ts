import {headers} from 'next/headers'

const api_server = process.env.API_SERVER

export async function GET(request: Request,
                          {params}: { params: { slug: string[] } }) {
  const {searchParams} = new URL(request.url)
  const headersList = headers()
  const authorization = headersList.get('Authorization')
  const url = api_server + '/organization/' + params.slug.join('/') + '?' + searchParams
  const response = await fetch(url, {
    method: 'GET',
    headers: {authorization}
  })
  return response
}

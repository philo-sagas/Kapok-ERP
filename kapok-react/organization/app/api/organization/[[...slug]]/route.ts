const api_server = process.env.API_SERVER

export async function GET(request: Request,
                          {params}: { params: { slug: string[] } }) {
  const {searchParams} = new URL(request.url)
  const requestHeaders = new Headers(request.headers)
  const url = api_server + '/organization/' + params.slug.join('/') + '?' + searchParams
  const response = await fetch(url, {
    method: 'GET',
    headers: requestHeaders
  })
  return response
}

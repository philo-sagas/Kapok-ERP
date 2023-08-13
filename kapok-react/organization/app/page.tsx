'use client'

import {useRouter, useSearchParams} from 'next/navigation'
import {obtainAccessToken} from '@/utils/authorization'

export default function Home() {
  const router = useRouter()
  const searchParams = useSearchParams()
  const code = searchParams.get('code')
  const state = searchParams.get('state')
  const error = searchParams.get('error')
  if (code && state) {
    obtainAccessToken(code, state).then(() => router.replace(state))
  } else if (error) {

  } else {
    router.replace('/dashboard')
  }
  return null;
}

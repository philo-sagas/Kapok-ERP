'use client'

import {useRouter} from 'next/navigation'
import {obtainAccessToken} from '@/utils/authorization'

export default function Home({searchParams}) {
  const router = useRouter()
  if (searchParams.code) {
    obtainAccessToken(searchParams.code, searchParams.state)
      .then(() => router.replace(searchParams.state))
  } else if (searchParams.error) {

  } else {
    router.replace('/dashboard')
  }
  return null;
}

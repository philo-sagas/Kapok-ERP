'use client'

import {useEffect, useState} from 'react'

interface Display {
  width: number,
  height: number
}

function useDisplay(): Display {
  const [display, setDisplay] = useState<Display>({
    width: 0,
    height: 0
  })

  useEffect(() => {
    const updateDisplaySize = () => setDisplay({
      width: window.innerWidth,
      height: window.innerHeight
    })
    updateDisplaySize()
    window.addEventListener('resize', updateDisplaySize)
    return () => window.removeEventListener('resize', updateDisplaySize)
  }, [])

  return display
}

export default useDisplay

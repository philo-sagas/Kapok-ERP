'use client'

import * as React from 'react';
import AppBar from '@/components/layouts/AppBar'
import Drawer from '@/components/layouts/Drawer'
import Box from '@mui/material/Box';
import CssBaseline from '@mui/material/CssBaseline';
import Toolbar from '@mui/material/Toolbar';

export default function DashboardLayout({children}: {
  children: React.ReactNode
}) {
  const [showDrawer, setShowDrawer] = React.useState(true);

  const toggleDrawer = () => {
    setShowDrawer((prevShowDrawer) => !prevShowDrawer);
  }

  return (
    <Box sx={{display: 'flex'}}>
      <CssBaseline/>
      <AppBar toggleDrawer={toggleDrawer}/>
      <Drawer toggleDrawer={toggleDrawer} showDrawer={showDrawer}/>
      <Box component="main" style={{flexGrow: 1}}>
        <Toolbar/>
        {children}
      </Box>
    </Box>
  )
}

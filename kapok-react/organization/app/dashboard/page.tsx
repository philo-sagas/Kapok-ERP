'use client'

import * as React from "react";
import Link from 'next/link';
import Grid from '@mui/material/Grid';
import Icon from '@mui/material/Icon';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import useAuthorization from '@/utils/hooks/useAuthorization';

export default function DashboardPage() {
  const {authMenus} = useAuthorization();

  return (
    <>
      <Grid
        container
        spacing={2}
        sx={{p: 2}}
      >
        {authMenus.map((node) => (
          <Grid key={node.code} item sm={12} md={6} lg={4}>
            <Link href={node.href}>
              <Paper sx={{p: 4, bgcolor: 'secondary.main'}}>
                <Typography variant="h4">
                  <Icon sx={{mr: 1}}>{node.icon}</Icon>
                  {node.name}
                </Typography>
              </Paper>
            </Link>
          </Grid>
        ))}
      </Grid>
    </>
  )
}

'use client'

import * as React from 'react';
import Box from '@mui/material/Box';
import List from '@mui/material/List';
import Divider from '@mui/material/Divider';
import Toolbar from '@mui/material/Toolbar';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Drawer from "@mui/material/Drawer";
import Icon from '@mui/material/Icon';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
import useAuthorization from '@/utils/hooks/useAuthorization';


const drawerWidth = 220;

interface Props {
  showDrawer: boolean,
  toggleDrawer: () => void
}

export default function DrawerLayout({showDrawer, toggleDrawer}: Props) {
  const {user, authMenus} = useAuthorization();
  const pathname = usePathname();

  return (
    <Drawer
      variant="permanent"
      ModalProps={{
        keepMounted: true, // Better open performance on mobile.
      }}
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        [`& .MuiDrawer-paper`]: {width: drawerWidth, boxSizing: 'border-box'}
      }}
    >
      <Toolbar/>
      <Box sx={{overflow: 'auto'}}>
        <List component="nav">
          <ListItem alignItems="flex-start">
            <ListItemAvatar>
              <Avatar alt="Current User" src="/avatar.jpg"/>
            </ListItemAvatar>
            <ListItemText
              primary={user.sub}
              secondary={user.name}
            />
          </ListItem>
          <Divider/>
          {authMenus.map((node) => (
            <Link key={node.code} href={node.href}>
              <ListItem disablePadding>
                <ListItemButton selected={node.href === pathname}>
                  <ListItemIcon>
                    <Icon>{node.icon}</Icon>
                  </ListItemIcon>
                  <ListItemText primary={node.name}/>
                </ListItemButton>
              </ListItem>
            </Link>
          ))}
        </List>
      </Box>
    </Drawer>
  )
}

import LinearProgress from '@mui/material/LinearProgress';
import Stack from '@mui/material/Stack';

export default function DashboardLoading() {
  return (
    <Stack sx={{width: '100%', color: 'grey.500'}} spacing={2}>
      <LinearProgress color="primary"/>
      <LinearProgress color="secondary"/>
      <LinearProgress color="error"/>
      <LinearProgress color="warning"/>
      <LinearProgress color="info"/>
      <LinearProgress color="success"/>
    </Stack>
  )
}

'use client'

import * as React from 'react';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import {
  DataGrid,
  GridActionsCellItem,
  GridColDef,
  GridPaginationModel,
  GridRenderCellParams,
  GridRowParams,
  GridSortModel
} from '@mui/x-data-grid';
import useDisplay from "@/utils/hooks/useDisplay";
import Fab from '@mui/material/Fab';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import SearchIcon from '@mui/icons-material/Search';
import Link from 'next/link';
import Chip from '@mui/material/Chip';
import Breadcrumbs from "@mui/material/Breadcrumbs";
import {defaultFetchToJson} from '@/utils/authorization'
import TextField from '@mui/material/TextField';
import useDictionary from "@/utils/hooks/useDictionary";

function handleAdd() {
  console.info('待实现！ - handleAdd: ' + arguments)
}

function handleEdit(row?) {
  console.info('待实现！ - handleEdit: ' + arguments)
}

function handleDelete(row?) {
  console.info('待实现！ - handleDelete: ' + arguments)
}

export default function UserPage() {
  const display = useDisplay();
  const enabledDict = useDictionary('Enabled')

  const [paginationModel, setPaginationModel] = React.useState<GridPaginationModel>({
    page: 0,
    pageSize: 10,
  });
  const [sortModel, setSortModel] = React.useState<GridSortModel[]>([]);
  const [rows, setRows] = React.useState([]);
  const [rowCount, setRowCount] = React.useState(0);
  const [isLoading, setIsLoading] = React.useState(false);
  const [keyword, setKeyword] = React.useState('');

  React.useEffect(() => {
    if (!isLoading) {
      loadData();
    }
  }, [paginationModel, sortModel]);

  function loadData() {
    const access_token = window.sessionStorage.getItem('access_token')
    const args = new URLSearchParams({
      keyword: keyword,
      page: paginationModel.page,
      size: paginationModel.pageSize,
      sort: sortModel.map(s => s.field + ',' + s.sort)[0] || ''
    })
    setIsLoading(true)
    fetch('/api/organization/v1/user?' + args, {
      method: 'GET',
      headers: {
        Authorization: 'Bearer ' + access_token
      }
    })
      .then(defaultFetchToJson)
      .then((data) => {
        if (data.status === 200) {
          setRows(data.data)
          setRowCount(data.total)
        }
      })
      .finally(() => {
        setIsLoading(false)
      })
  }

  const columns: GridColDef[] = [
    {
      field: 'subject',
      headerName: '用户账号',
      headerAlign: 'center',
      sortable: true
    },
    {
      field: 'username',
      headerName: '用户名称',
      headerAlign: 'center',
      sortable: false
    },
    {
      field: 'enabled',
      headerName: '用户状态',
      headerAlign: 'center',
      align: 'center',
      sortable: false,
      renderCell: (params: GridRenderCellParams<boolean>) => (
        <Chip label={enabledDict.mapping[params.value]} color={params.value ? 'success' : 'default'}/>
      )
    },
    {
      field: 'phoneNumber',
      headerName: '手机号码',
      headerAlign: 'center',
      sortable: false
    },
    {
      field: 'email',
      headerName: '电子邮件',
      headerAlign: 'center',
      minWidth: 100,
      flex: 1,
      sortable: false
    },
    {
      field: 'birthdate',
      headerName: '出生日期',
      headerAlign: 'center',
      sortable: false
    },
    {
      field: 'createdDate',
      headerName: '创建时间',
      headerAlign: 'center',
      minWidth: 153,
      sortable: false
    },
    {
      field: 'createdBy',
      headerName: '创建用户',
      headerAlign: 'center',
      minWidth: 110,
      sortable: false
    },
    {
      field: 'actions',
      headerName: '操作',
      type: 'actions',
      sortable: false,
      width: 120,
      getActions: (params: GridRowParams) => [
        <GridActionsCellItem icon={<EditIcon fontSize="inherit"/>} onClick={handleEdit} label="Edit"/>,
        <GridActionsCellItem icon={<DeleteIcon fontSize="inherit"/>} onClick={handleDelete} label="Delete"/>
      ]
    }
  ];

  return (
    <>
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          alignContent: 'space-around',
          p: 1
        }}
      >
        <Breadcrumbs aria-label="breadcrumb" sx={{flexGrow: 1}}>
          <Link underline="hover" color="inherit" href="/dashboard">
            Home
          </Link>
          <Typography color="text.primary">用户管理</Typography>
        </Breadcrumbs>
        <Box sx={{flexShrink: 1}}>
          <TextField
            label="关键字"
            variant="standard"
            value={keyword}
            onChange={(event) => setKeyword(event.target.value)}
          />
          <Fab
            sx={{ml: 1}}
            size="medium"
            color="primary"
            aria-label="search"
            onClick={loadData}
          >
            <SearchIcon/>
          </Fab>
          <Button
            sx={{ml: 1}}
            variant="outlined"
            onClick={handleAdd}
            startIcon={<AddIcon/>}
          >
            新增
          </Button>
          <Button
            sx={{ml: 1}}
            variant="outlined"
            onClick={handleDelete}
            startIcon={<DeleteIcon/>}
          >
            删除
          </Button>
        </Box>
      </Box>
      <DataGrid
        rows={rows}
        columns={columns}
        rowCount={rowCount}
        loading={isLoading}
        paginationModel={paginationModel}
        onPaginationModelChange={setPaginationModel}
        pageSizeOptions={[10, 25, 50, 100]}
        sortModel={sortModel}
        onSortModelChange={setSortModel}
        paginationMode="server"
        sortingMode="server"
        checkboxSelection
        sx={{
          height: () => display.height - 128
        }}
      />
    </>
  );
}

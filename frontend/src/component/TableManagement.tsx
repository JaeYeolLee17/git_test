import * as React from 'react';
import { 
  DataGrid,
  gridPageCountSelector,
  gridPageSelector,
  useGridApiContext,
  useGridSelector,
  GridCellParams, 
  GridToolbarQuickFilter } from '@mui/x-data-grid';
import Box from '@mui/material/Box';
import styles from './TableManagement.module.css';
import Pagination from '@mui/material/Pagination';

type rows = {
  id: string,
  region: string,
  intersection: string,
  cameraDirection: string
}

type columns = {
  field: string,
  headerName: string,
  flex: number
}

type clickEvent = (cameraId: string) => void;

function CustomPagination() {
  const apiRef = useGridApiContext();
  const page = useGridSelector(apiRef, gridPageSelector);
  const pageCount = useGridSelector(apiRef, gridPageCountSelector);

  return (
    <Pagination
      color="primary"
      count={pageCount}
      page={page + 1}
      onChange={(event, value) => apiRef.current.setPage(value - 1)}
    />
  );
}

function QuickSearchToolbar() {
  return (
    <Box
      sx={{
        p: 0.5,
        pb: 0,
        textAlign: 'left'
      }}
    >
      <GridToolbarQuickFilter />
    </Box>
  );
}

function TableManagement({rows, columns, clickEvent} : {rows: any[], columns:columns[], clickEvent:clickEvent}) {

  return (
    <Box className={styles.box}>
        <DataGrid
          rows={rows}
          columns={columns}
          pagination
          pageSize={10}
          rowsPerPageOptions={[10]}
          components={{
            Pagination: CustomPagination,
            Toolbar: QuickSearchToolbar
          }}
          onRowClick={(e) => clickEvent(e.row.id)}
          className={styles.datagrid}
          scrollbarSize={0}
          rowHeight={60}
          autoHeight
          sx={{
            border: 0,
            borderRadius: 2,
            fontSize: 16,
            alignContent: 'center',
            p: 2,
            textAlign: 'center',
            "& .MuiDataGrid-columnHeaders": {
              backgroundColor: "#f8fafb",
            },
            "& .MuiDataGrid-cell" : {
            },
            "& .MuiDataGrid-row.Mui-selected" : {
              backgroundColor: '#4578bd',
              color: 'white'
            },
            "& .MuiDataGrid-row.Mui-selected:hover" : {
              backgroundColor: '#4578bd',
              color: 'white'
            }
          }}
        />
      </Box>
  );
}

export default TableManagement
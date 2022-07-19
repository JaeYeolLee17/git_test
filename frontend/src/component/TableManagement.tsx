import * as React from 'react';
import { DataGrid } from '@mui/x-data-grid';

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

function TableManagement({rows, columns, clickEvent} : {rows: any[], columns:columns[], clickEvent:clickEvent}) {

  return (
      <DataGrid
        rows={rows}
        columns={columns}
        pageSize={10}
        onRowClick={(e) => clickEvent(e.row.id)}
      />
  );
}

export default TableManagement
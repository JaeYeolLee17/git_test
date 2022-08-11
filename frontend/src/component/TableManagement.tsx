import React, {useRef, useCallback} from "react";
import Box from "@mui/material/Box";
import SearchIcon from '@mui/icons-material/Search';

import { AgGridReact } from 'ag-grid-react';

import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-alpine.css';
import styles from "./TableManagement.module.css";

import "./Table.css";

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

function TableManagement({rows, columns, selectedId, clickEvent} : {rows: any[], columns:columns[], selectedId:string, clickEvent:clickEvent}) {
  const gridRef = useRef<AgGridReact<any>>(null);

  React.useEffect(() => {
    selectedId !== "" &&
      gridRef.current!.api.forEachNode(function(node) {
        node.setSelected(node.data!.id === selectedId);
        if(node.data!.id === selectedId){
          gridRef.current!.api.paginationGoToPage(Math.floor(node.childIndex / 10));
        }
      });
  }, [selectedId])

  const onQuickFilterChanged = useCallback(() => {
    gridRef.current!.api.setQuickFilter(
      (document.getElementById('quickFilter') as HTMLInputElement).value
    );
  }, []);

  return (
    <Box className={styles.box}>
      <div className={styles.filter}>
        <input
          type="text"
          onInput={onQuickFilterChanged}
          id="quickFilter"
          placeholder="검색"
        />
        <SearchIcon className={styles.img}/>
      </div>
      <div className="ag-theme-alpine" style={{height: '800px', width: '100%'}}>
        <AgGridReact
          ref={gridRef}
          defaultColDef={{
            editable: false,
            sortable: false,
            filter: false,
            resizable: true,
            cellClass: styles.cell,
            headerClass: styles.header
          }}
          headerHeight={60}
          rowClass={styles.wrapper}
          rowHeight={60}
          rowData={rows}
          columnDefs={columns}
          rowSelection={"single"}
          pagination={true}
          paginationPageSize={10}
          onRowClicked={(e) => clickEvent(e.api.getSelectedRows()[0].id)}
        />
      </div>
    </Box>
  );
}

export default TableManagement
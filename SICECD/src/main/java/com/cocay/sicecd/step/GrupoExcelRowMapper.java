package com.cocay.sicecd.step;

import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;

import com.cocay.sicecd.model.Grupo;

public class GrupoExcelRowMapper implements RowMapper<Grupo>{

	@Override
	public Grupo mapRow(RowSet rs) throws Exception {
		if(rs == null || rs.getCurrentRow() == null) {
			return null;
		}
		
		Grupo grupo = new Grupo();
		grupo.setClave(rs.getColumnValue(0));
		grupo.setTempCurso(rs.getColumnValue(1));
		grupo.setTempProfesor(rs.getColumnValue(2));
		
		return grupo;
	}

}

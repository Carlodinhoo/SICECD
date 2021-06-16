package com.cocay.sicecd.step;

import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;

import com.cocay.sicecd.model.Inscripcion;

public class InscripcionExcelRowMapper implements RowMapper<Inscripcion>{

	@Override
	public Inscripcion mapRow(RowSet rs) throws Exception {
		if(rs == null || rs.getCurrentRow() == null) {
			return null;
		}
		
		Inscripcion inscripcion = new Inscripcion();
		inscripcion.setTempGrupo(rs.getColumnValue(0));
		inscripcion.setTempCurso(rs.getColumnValue(1));
		inscripcion.setTempProfesor(rs.getColumnValue(2));
		inscripcion.setCalif(rs.getColumnValue(3));
		
		return inscripcion;
	}

}

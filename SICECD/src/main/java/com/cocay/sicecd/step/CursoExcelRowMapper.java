package com.cocay.sicecd.step;

import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;

import com.cocay.sicecd.dto.CursoDto;
import com.cocay.sicecd.model.Curso;

public class CursoExcelRowMapper implements RowMapper<Curso>{

	@Override
	public Curso mapRow(RowSet rowSet) throws Exception {
		if(rowSet == null || rowSet.getCurrentRow() == null) {
			return null;
		}
		
		Curso curso = new Curso();
		curso.setClave(rowSet.getColumnValue(0));
		curso.setNombre(rowSet.getColumnValue(1));
		curso.setTempTipoCurso(rowSet.getColumnValue(2));
		double d = Double.parseDouble(rowSet.getColumnValue(3));
		int horas = (int)d;
		curso.setHoras(horas);
		
		System.out.println("Row: "+rowSet.getColumnValue(0)+", "+rowSet.getColumnValue(1)+", "+rowSet.getColumnValue(2)+", "+rowSet.getColumnValue(3));
		System.out.println("Curso :"+curso.getClave()+", "+curso.getNombre()+", "+curso.getTempTipoCurso()+", "+curso.getHoras());
		// TODO Auto-generated method stub
		return curso;
	}

}

package com.cocay.sicecd.step;

import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;

import com.cocay.sicecd.model.Profesor;

public class ProfesorExcelRowMapper implements RowMapper<Profesor>{

	@Override
	public Profesor mapRow(RowSet rs) throws Exception {
		if(rs == null || rs.getCurrentRow() == null) {
			return null;
		}
		
		Profesor profesor = new Profesor();
		profesor.setNombre(rs.getColumnValue(0));
		profesor.setApellido_paterno(rs.getColumnValue(1));
		profesor.setApellido_materno(rs.getColumnValue(2));
		profesor.setRfc(rs.getColumnValue(3));
		profesor.setCorreo(rs.getColumnValue(4));
		profesor.setTelefono(rs.getColumnValue(5));
		profesor.setTempEstado(rs.getColumnValue(6));
		profesor.setCiudad_localidad(rs.getColumnValue(7));
		profesor.setTempGenero(rs.getColumnValue(8));
		profesor.setPlantel(rs.getColumnValue(9));
		profesor.setClave_plantel(rs.getColumnValue(10));
		profesor.setTempTurno(rs.getColumnValue(11));
		profesor.setTempGradoP(rs.getColumnValue(12));
		profesor.setOcupacion(rs.getColumnValue(13));
		
		return profesor;
	}

}

var url_local = window.location.href;
var arr = url_local.split("/");
var fin = arr[0]+"//"+arr[2];

$(document).ready(function(){
	llenarTablaGrupo();
	llenarTablaErrores();
});

function llenarTablaGrupo(){
	$('#tablaGrupo').bootstrapTable({
		search: false,
		pagination: true,
		pageSize: 5,
		pageList: [5, 25, 50],
		clickToSelect: false,
		singleSelect: false,
		maintainSelected: true,
		sortable: true,
		checkboxHeader: false,
//		data: [],
		sidePagination: 'server',
		url: fin+"/rest/CargaBatchService/lstGrupo",
		queryParams: 'queryParams',
		responseHandler : "responseHandler",	
		formatShowingRows: function (pageFrom, pageTo, totalRows) {
			return 'Mostrando ' + pageFrom + ' al ' + pageTo + ' de ' + totalRows + ' registros';
		},
		formatRecordsPerPage: function (pageNumber) {
			return pageNumber + ' registros por p\u00e1gina';
		},
		formatLoadingMessage: function () {
			return 'Cargando, espere por favor...';
		},
		formatSearch: function () {
			return 'Buscar';
		},
		formatNoMatches: function () {
			return 'No se encontr&oacute; informaci&oacute;n';
		}
	});
}

function queryParams(params) {
    return {
        limit: params.limit,
        offset: params.offset,
        search: params.search,
        name: params.sort,
        order: params.sort != undefined && params.sort != null ? params.order : "DESC"
    };
}

function responseHandler(res) {
	return {
        rows: res.response.rows,
        total: res.response.total
    };
}

function llenarTablaErrores(){
	$('#tablaErrores').bootstrapTable({
		search: false,
		pagination: true,
		pageSize: 5,
		pageList: [5, 25, 50],
		clickToSelect: false,
		singleSelect: false,
		maintainSelected: true,
		sortable: true,
		checkboxHeader: false,
//		data: [],
		sidePagination: 'server',
		url: fin+"/rest/CargaBatchService/lstErrores",
		queryParams: 'queryParams',
		responseHandler : "responseHandler",	
		formatShowingRows: function (pageFrom, pageTo, totalRows) {
			return 'Mostrando ' + pageFrom + ' al ' + pageTo + ' de ' + totalRows + ' registros';
		},
		formatRecordsPerPage: function (pageNumber) {
			return pageNumber + ' registros por p\u00e1gina';
		},
		formatLoadingMessage: function () {
			return 'Cargando, espere por favor...';
		},
		formatSearch: function () {
			return 'Buscar';
		},
		formatNoMatches: function () {
			return 'No se encontr&oacute; informaci&oacute;n';
		}
	});
}

function limpiarErrores(){
	return $.ajax({
		url: fin+"/rest/CargaBatchService/lstLimpiar",
		type : 'POST',
		contentType: 'application/json',
		dataType: 'JSON',
		async: false
	});
}

function btnLimpiar(){
	limpiarErrores().done(function (data){
		$('#tablaErrores').bootstrapTable('refresh');
	});
}

function limpiarTabla(Grupo){
	return $.ajax({
		url: fin+"/rest/CargaBatchService/limpiarTabla/"+Grupo,
		type : 'GET',
		async: false
	});
}

function btnLimpiar2(){
	var tabla = "Grupo";
	limpiarTabla(tabla).done(function (data){
		$('#tablaGrupo').bootstrapTable('refresh');
		
	});
}


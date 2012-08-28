package com.welocally.admin.spreadsheet.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.welocally.admin.domain.Index;
import com.welocally.admin.mvc.controller.UserController;
import com.welocally.admin.mvc.model.GeodbGeometry;
import com.welocally.admin.mvc.model.GeodbIndexModel;
import com.welocally.admin.mvc.model.GeodbIndexRecord;
import com.welocally.admin.mvc.model.GeodbIndexRecords;
import com.welocally.admin.mvc.model.GeodbIndexSchema;
import com.welocally.admin.mvc.model.NameValue;
import com.welocally.admin.mvc.model.SchemaProperty;

@Component
public class SpreadsheetTransformationUtils {
    private static final Logger logger = Logger.getLogger(SpreadsheetTransformationUtils.class);
    
    public Map<Integer,String> makeColumnNames(ExportRow firstRow){
        Map<Integer,String> names = new HashMap<Integer,String>();
        List<ExportColumn> cols = firstRow.getColumns();
        for (ExportColumn exportColumn : cols) {
            names.put(exportColumn.getColnum(),exportColumn.getText());
        }
        return names;
    }
    
    public List<ExportRow> makeExportFromWorksheetCellFeed(WorksheetCellFeed feed) throws IOException{
        Map<Integer,ExportRow> erows = new TreeMap<Integer,ExportRow>();
        
        for (CellEntry entry : feed.entry) {
            ExportRow row = erows.get(entry.getCell().getRow());
            
            if(row == null){
                row = new ExportRow();
                erows.put(entry.getCell().getRow(),row);
            }
            row.putCell(entry.getCell().getCol(),entry.getTitle().getName(), entry.getCell());          
        }       
        return new ArrayList<ExportRow>(erows.values());
    }
    
    public WorksheetGrid makeWorksheetGridFromWorksheetCellFeed(WorksheetCellFeed feed) throws IOException{
        WorksheetGrid grid = new WorksheetGrid();
        grid.setId(feed.getId().getValue());
        grid.setName(feed.getTitle().getName());       
        grid.setRows(makeExportFromWorksheetCellFeed(feed));
        grid.setColNames(makeColumnNames(grid.getRows().get(0)));
        return grid;
    }
    
    public GeodbIndexModel makeIndexModel(WorksheetGrid grid, Index index){   
        
        GeodbIndexModel model = new GeodbIndexModel();
        model.setIndexId(index.getWorksheetFeed());
        
        String[] searchfields = index.getSearchFields().split(",");  
        
        //make the schema
        GeodbIndexSchema schema = new GeodbIndexSchema();
        
        //id
        schema.setIdField(new SchemaProperty(index.getPrimaryKey(), false));
        
        //properties
        Map<Integer,String> colnames = grid.getColNames();
        for (Integer colNumber : colnames.keySet()) {
            String columnNameForCol = colnames.get(colNumber);
            schema.getProperties().add(
                    new SchemaProperty(columnNameForCol, found(searchfields, columnNameForCol))); 
            
        }
                    
        model.setSchema(schema);
         
        //make the data
        GeodbIndexRecords records = new GeodbIndexRecords();
        
        //skip headings
        Iterator<ExportRow> iterator = grid.getRows().iterator();
        iterator.next();
        while (iterator.hasNext()) {
            ExportRow row = iterator.next();
            GeodbIndexRecord record = new GeodbIndexRecord();
            record.setId(makeId(row, index,grid.getColNames()));
            record.setProperties(makeProperties(row, index,grid.getColNames()));
            record.setGeometry(makeGeometry( row,  index,grid.getColNames()));            
            records.getRecords().add(record);           
        }
 
        model.setData(records);
        
        return model;
    }
    
    protected String makeId(ExportRow row, Index index, Map<Integer,String> colnames){
        String pk = index.getPrimaryKey();
        for (ExportColumn col : row.getColumns()) {
            if(colnames.get(col.getColnum()).equals(pk)){
                return col.getText();
            }
        }
        throw new RuntimeException("primary key not found");
    }
    
    protected List<NameValue> makeProperties(ExportRow row, Index index, Map<Integer,String> colnames){
        List<NameValue> properties = new ArrayList<NameValue>();       
        for (ExportColumn col : row.getColumns()) {
            properties.add(new NameValue(colnames.get(col.getColnum()),col.getText()));
        }
        
        return properties;
        
    }
    
    protected GeodbGeometry makeGeometry(ExportRow row, Index index,Map<Integer,String> colnames){
        GeodbGeometry geom = new GeodbGeometry();
        geom.setType("Point");
        //find the lat and lng values
        Double[] coord = new Double[2];
        for (ExportColumn col : row.getColumns()) {
            
            
            logger.debug(colnames.get(col.getColnum())+","+index.getLng());
            
            if(colnames.get(col.getColnum()).equals(index.getLat())){
                logger.debug("SET LAT:"+colnames.get(col.getColnum())+","+index.getLat()+","+col.getText());
                coord[1] = Double.parseDouble(col.getText());               
            } else if(colnames.get(col.getColnum()).equals(index.getLng())){
                logger.debug("SET LNG:"+colnames.get(col.getColnum())+","+index.getLat()+","+col.getText());
                coord[0] = Double.parseDouble(col.getText());    
            }
            
        }
        geom.setCoordinates(coord);
        
        
        return geom;
    }
    
    protected boolean found(String[] searchfields, String item) {
        for (int i = 0; i < searchfields.length; i++) {
            logger.debug(searchfields+","+item);
            if(searchfields[i].equals(item))
                return true;
        }
        return false;
        
    }

}

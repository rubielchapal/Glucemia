package model;

import Dao.DAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartSeries;

/**
 * show data glucose on graphic
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
@ManagedBean
@ViewScoped
public class ChartView implements Serializable {

    private LineChartModel Grafica = new LineChartModel();
    private LineChartModel GraficaTimeLine = new LineChartModel();    
    private String fecha = "";
    private String width;

    /**
     * 
     */
    public ChartView() {

    }

    /**
     * render  graphic
     * @param ndivalue
     * @throws ParseException
     */
    public void ShowLineChart(String ndivalue) throws ParseException {

        DAO dao = new DAO();
        List<ListGluc> listGlucose = new ArrayList<>();

        try {
            dao.conectar();
            listGlucose = dao.listGlucose(ndivalue);
            dao.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }

        ListGraphic vector;
        List<ListGraphic> grafico = new ArrayList<>();

        for (ListGluc item : listGlucose) {
            if (getFecha().equals((item.getIssued()).substring(0, 8))) {
                vector = new ListGraphic();
                vector.setDate(getFecha());
                vector.setHour(item.getIssued().substring(9, 14));
                vector.setValue(item.getValueQuantity());
                grafico.add(vector);
            }
        }

        if (grafico != null && !grafico.isEmpty()) {
            LineChartModel model = new LineChartModel();
            ChartSeries series = new ChartSeries();

            for (ListGraphic item : grafico) {
                series.set(item.getHour(), (Float.parseFloat(item.getValue())) * 18);
            }

            series.setLabel("Glucosa");
            model.addSeries(series);
            model.setTitle("Grafico de medidas");
            model.setLegendPosition("e");
            model.setShowPointLabels(true);
            model.getAxes().put(AxisType.X, new CategoryAxis("HORA"));

            Axis yAxis = model.getAxis(AxisType.Y);
            yAxis.setLabel("mg/dl");

            setGrafica(model);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existen medidas para esa fecha", null));
        }

    }
    
    /**
     * render  graphic Time line
     * @param ndivalue
     * @throws ParseException
     */
    public void ShowTimeLineChart(String ndivalue) throws ParseException {

        DAO dao = new DAO();
        List<ListGluc> listGlucose = new ArrayList<>();

        try {
            dao.conectar();
            listGlucose = dao.listGlucose(ndivalue);
            dao.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Respuesta: ", ex.toString()));
        }

        ListGraphic vector;
        List<ListGraphic> grafico = new ArrayList<>();

        for (ListGluc item : listGlucose) {            
                vector = new ListGraphic();
                vector.setDate(((item.getIssued()).substring(0, 8)).replace("/","-"));
                vector.setHour(item.getIssued().substring(9, 14));
                vector.setValue(item.getValueQuantity());
                grafico.add(vector);           
        }

        if (grafico != null && !grafico.isEmpty()) {
            LineChartModel model = new LineChartModel();
            LineChartSeries series = new LineChartSeries();
            String date;

            for (ListGraphic item : grafico) {
                date = item.getDate().substring(3, 5)+"/"+item.getDate().substring(0, 2)+"/"+item.getDate().substring(6, 8)+" "+item.getHour();
                series.set(date, (Float.parseFloat(item.getValue())) * 18);
           
            }
        series.setLabel("Series 1");
        model.addSeries(series);         
        model.setTitle("Histórico de medidas");
        model.setZoom(true);
        model.getAxis(AxisType.Y).setLabel("Values");
        DateAxis axis = new DateAxis("Fecha de medidas");
        axis.setTickAngle(-50); 
        axis.setTickFormat("%m/%d/%y %H:%M");         
        model.getAxes().put(AxisType.X, axis);   
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel("Glucosa (mg/dl)");
        setWidth(""+grafico.size()*100);
        setGraficaTimeLine(model);       
            
         
    
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existen medidas para esa fecha", null));
        }

    }

    /**
     * @return the Grafica
     */
    public LineChartModel getGrafica() {
        return Grafica;
    }

    /**
     * @param Grafica the Grafica to set
     */
    public void setGrafica(LineChartModel Grafica) {
        this.Grafica = Grafica;
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the GraficaTimeLine
     */
    public LineChartModel getGraficaTimeLine() {
        return GraficaTimeLine;
    }

    /**
     * @param GraficaTimeLine the GraficaTimeLine to set
     */
    public void setGraficaTimeLine(LineChartModel GraficaTimeLine) {
        this.GraficaTimeLine = GraficaTimeLine;
    }

    /**
     * @return the width
     */
    public String getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(String width) {
        this.width = width;
    }

}

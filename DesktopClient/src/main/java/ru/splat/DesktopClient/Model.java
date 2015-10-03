package ru.splat.DesktopClient;


import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;


/**
 * <p>
 *
 * @author Ekaterina
 *         <p>
 *         Model of MVC
 */
public class Model implements Subject
{
    private static final Logger log = LoggerFactory.getLogger(Model.class);

    private ArrayList<Observer> observers = new ArrayList<Observer>();

    private com.google.common.collect.Table<Integer, Timestamp, ProviderPackage> modelTable = TreeBasedTable.create();

    private ProviderType providerType = ProviderType.PROVIDER_XML;

    private int id = 0;

    public void setProviderType(ProviderType providerType) {
    	this.providerType = providerType;
    }
    
    public void setId(int id) {
    	this.id = id;
    }
    
    public ProviderType getProviderType() {
    	return providerType;
    }
    
    public int getId() {
    	return id;
    }
    
    public com.google.common.collect.Table getModelTable() {
    	return modelTable;
    }

    @Override public void registerObserver(Observer o)
    {
        observers.add(o);
    }


    @Override public void removeObserver(Observer o)
    {
        observers.remove(o);
    }


    @Override public void notifyAllObserver()
    {
        for (Observer o : observers)
        {
            o.update();
        }
    }


    @Override public com.google.common.collect.Table<Integer, Timestamp, ProviderPackage> getModel()
    {
        return modelTable;
    }

}

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

    private com.google.common.collect.Table<Integer, Timestamp, ProviderPackage> model = TreeBasedTable.create();

    private int providerId = 0; // 0 - xml; 1 - json

    private int id = 0;

    public void setProviderId(int providerId) {
    	this.providerId = providerId;
    }
    
    public void setId(int id) {
    	this.id = id;
    }
    
    public int getProviderId() {
    	return providerId;
    }
    
    public int getId() {
    	return id;
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

        return model;

    }

}

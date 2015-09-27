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

    public com.google.common.collect.Table<Integer, Timestamp, ProviderPackage> Model = TreeBasedTable.create();

    public int providerId = 0; // 0 - xml; 1 - json

    public int id = 0;


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


    public Table<Integer, Timestamp, ProviderPackage> getModel()
    {
        return Model;
    }


    public void setModel(Table<Integer, Timestamp, ProviderPackage> model)
    {
        Model = model;
        notifyAllObserver();
    }
}

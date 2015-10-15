package ru.splat.DesktopClient;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.TreeBasedTable;


/**
 * <p>
 *
 * @author Andrey & Ekaterina
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


    public void setProviderType(ProviderType providerType)
    {
        this.providerType = providerType;
    }


    public void setId(int id)
    {
        this.id = id;
    }


    public ProviderType getProviderType()
    {
        return providerType;
    }


    public int getId()
    {
        return id;
    }

    public Set<Timestamp> getKeySetForRow(int row) 
    {
	return modelTable.row(row).keySet();
    }
    
    public ProviderPackage getPackageForRowAndTime(int row, Timestamp time) 
    {
	return modelTable.row(row).get(time);
    }
    
    public void addPackage(int providerId, Timestamp time, ProviderPackage providerPackage) 
    {
	modelTable.row(providerId).put(time, providerPackage);
    }
    
//    public com.google.common.collect.Table getModelTable()
//    {
//        return modelTable;
//    }


    @Override
    public void registerObserver(Observer o)
    {
        observers.add(o);
    }


    @Override
    public void removeObserver(Observer o)
    {
        observers.remove(o);
    }


    @Override
    public void notifyAllObserver(OperationType operation, int providerId, int packageId)
    {
        for (Observer o : observers)
        {
            o.update(operation, providerId, packageId);
        }
    }


//    @Override
//    public com.google.common.collect.Table<Integer, Timestamp, ProviderPackage> getModel()
//    {
//        return modelTable;
//    }

}

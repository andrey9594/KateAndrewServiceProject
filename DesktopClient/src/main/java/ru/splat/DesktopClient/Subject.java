package ru.splat.DesktopClient;


import java.sql.Timestamp;


/**
 * <p>
 *
 * @author Ekaterina
 */
public interface Subject
{
    public void registerObserver(Observer o);


    public void removeObserver(Observer o);


    public void notifyAllObserver();


    public com.google.common.collect.Table<Integer, Timestamp, ProviderPackage> getModel();
}

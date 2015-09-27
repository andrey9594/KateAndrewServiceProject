package ru.splat.DesktopClient;


import com.google.common.collect.Table;

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

    public Table<Integer, Timestamp, ProviderPackage> getModel();

    public void setModel(Table<Integer, Timestamp, ProviderPackage> model);
}


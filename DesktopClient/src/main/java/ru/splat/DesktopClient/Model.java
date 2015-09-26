package ru.splat.DesktopClient;


import com.google.common.collect.TreeBasedTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;


/**
 * <p>
 *
 * @author Ekaterina
 *         <p>
 *         Model of MVC
 */
public class Model
{
    private static final Logger log = LoggerFactory.getLogger(Model.class);

    public com.google.common.collect.Table<Integer, Timestamp, ProviderPackage> Model = TreeBasedTable.create();

    public int providerId = 0; // 0 - xml; 1 - json

    public int id;
}

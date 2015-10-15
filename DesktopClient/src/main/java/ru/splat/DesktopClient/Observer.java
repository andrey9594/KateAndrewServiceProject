package ru.splat.DesktopClient;


/**
 * <p>
 *
 * @author Ekaterina
 */
public interface Observer
{
    public void update(OperationType operation, int providerId, int packageId);
}

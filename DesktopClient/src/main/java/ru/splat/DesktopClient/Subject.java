package ru.splat.DesktopClient;

/**
 * <p>
 *
 * @author Ekaterina
 */
public interface Subject
{
    public void registerObserver(Observer o);


    public void removeObserver(Observer o);


    public void notifyAllObserver(OperationType operation, int providerId, int packageId);
}

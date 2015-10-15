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


    public void notifyAllObserver(int providerId, int packageId);
}

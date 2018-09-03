/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.UUID;
import java.util.function.Consumer;
import mgf.tr.scenario.Scenario;

/**
 *
 * @author Asus
 */
public final class EntityManager implements Iterable<Entity>
{
    private final HashMap<UUID, Entity> map = new HashMap<>();
    private final LinkedList<Entity> list = new LinkedList<>();
    private final LinkedList<Entity> toInsert = new LinkedList<>();
    private final LinkedList<UUID> toRemove = new LinkedList<>();
    
    public final void addEntity(Entity e) { toInsert.add(e); }
    
    public final boolean hasEntity(UUID id) { return map.containsKey(id); }
    
    public final boolean hasEntity(Entity e) { return map.containsKey(e.getId()); }
    
    public final Entity getEntity(UUID id) { return map.get(id); }
    
    public final void removeEntity(Entity e) { toRemove.add(e.getId()); }
    
    public final void removeEntity(UUID id) { toRemove.add(id); }
    
    public final void update(Scenario stage)
    {
        /* Insert entities */
        while(!toInsert.isEmpty())
        {
            Entity e = toInsert.removeFirst();
            if(map.containsKey(e.getId()))
                continue;
            list.add(e);
            map.put(e.getId(), e);
            e.setScenario(stage);
        }
        
        /* Remove entities */
        while(!toRemove.isEmpty())
        {
            UUID id = toRemove.removeFirst();
            Entity e = map.remove(id);
            if(e == null)
                continue;
            list.remove(e);
            e.setScenario(null);
        }
    }
    
    public final void forEachEntity(Consumer<Entity> action)
    {
        ListIterator<Entity> it = list.listIterator();
        while(it.hasNext())
        {
            Entity e = it.next();
            if(e.hasDestroyed())
            {
                it.remove();
                continue;
            }
            action.accept(e);
            if(e.hasDestroyed())
            {
                it.remove();
                continue;
            }
        }
    }
    
    @Override
    public final Iterator<Entity> iterator() { return list.iterator(); }
}

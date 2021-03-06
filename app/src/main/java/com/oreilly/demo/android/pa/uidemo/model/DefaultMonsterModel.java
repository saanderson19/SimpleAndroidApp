package com.oreilly.demo.android.pa.uidemo.model;

import com.oreilly.demo.android.pa.uidemo.MonsterModel;
import com.oreilly.demo.android.pa.uidemo.ModelListener;
import com.oreilly.demo.android.pa.uidemo.MonsterStateListener;
import com.oreilly.demo.android.pa.uidemo.model.cell.Cell;
import com.oreilly.demo.android.pa.uidemo.model.cell.CellWorld;
import com.oreilly.demo.android.pa.uidemo.model.monster.Actor;
import com.oreilly.demo.android.pa.uidemo.model.monster.Monster;
import com.oreilly.demo.android.pa.uidemo.model.timer.CreateNewMonsters;
import com.oreilly.demo.android.pa.uidemo.model.timer.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


/**
 * Created by Sarah on 5/3/2017.
 */

/*
* this is the default model for a monster and the one that is used to initialize game
* similar idea to having default stopwatch model.
* */

public class DefaultMonsterModel extends Activity implements MonsterModel {

    private ModelListener modellistener;
    private MonsterStateListener monsterlistener;
    private CellWorld cellworld;
    ArrayList<Actor> liveactors;
    private CreateNewMonsters creator = new CreateNewMonsters();

    public DefaultMonsterModel(Cell[][] grid,ModelListener modellistener){
        setModelListener(modellistener);
        cellworld = new CellWorld(grid);
        liveactors = new ArrayList<Actor>();
        creator.setOnTimerListener(this);
    }

    public CellWorld getWorld(){ return cellworld; }

    @Override
    public void setModelListener(ModelListener listener){
        this.modellistener = listener;
    }

    @Override
    public void setMonsterStateListener(MonsterStateListener listener){
        this.monsterlistener = listener;
    }


    //ontick, we add an actor at whatever time interval the ontick method is set to
    //this isn't working right now though, new monsters are not being added periodically
    //I think the new monsters aren't being caught by the default

    @Override
    public void onTick(Time observer){
        Random random = new Random();
        Monster monster = new Monster();
        monster.setMonsterStateChangeListener(this); //set this new monsters listener to the default
        int x = random.nextInt(cellworld.getGrid().length); //get random x, y coordinates in grid
        int y = random.nextInt(cellworld.getGrid()[0].length);
        cellworld.addActor(monster, x, y);
        liveactors.add(monster);
        monster.start();
    }

    @Override
    public void addActor(Actor actor, int x, int y){
        cellworld.addActor(actor, x, y);
    }

    @Override
    public void addLiveActor(Actor actor){
        liveactors.add(actor);
    }

    @Override
    public List<Actor> getLiveActors(){
        return liveactors;
    }

    @Override
    public void removeLiveActor(Actor actor){liveactors.remove(actor);}

    //@Override
    public void noMoreMonsters()
    {
        if (liveactors.isEmpty())
        {
            Context context = getBaseContext();
            CharSequence text = "You WIN!!!!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }


    //let model know to change state
    @Override
    public void onMSChange(int stateid){
        modellistener.onMSChange(stateid);
    }
}

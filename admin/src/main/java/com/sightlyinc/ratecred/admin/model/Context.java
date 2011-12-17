package com.sightlyinc.ratecred.admin.model;



public class Context
{
    private String state;

    public Context(String state)
    {
        this.state = state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public boolean isState(String state)
    {
        return this.state.equals( state );
    }

    public String toString()
    {
        return "{state=" + state + "}";
    }
}
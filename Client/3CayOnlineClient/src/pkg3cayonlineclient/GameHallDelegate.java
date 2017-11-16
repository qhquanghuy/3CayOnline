/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

/**
 *
 * @author HuyNguyen
 */
public interface GameHallDelegate {
    public void onTapBtnProfile();
    public void onTapBtnSignOut();
    public void joinARoom(int roomIdx);
    public void onTapBtnCreate();
}

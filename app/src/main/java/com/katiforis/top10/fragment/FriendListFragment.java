package com.katiforis.top10.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.katiforis.top10.DTO.Player;
import com.katiforis.top10.R;
import com.katiforis.top10.adapter.FriendAdapter;

import java.util.ArrayList;
import java.util.List;

public class FriendListFragment extends Fragment {
    private RecyclerView friendsRecyclerView;
    private FriendAdapter friendAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Player> friends = new ArrayList<>();


    public FriendListFragment() {}


    public static FriendListFragment newInstance(int title) {
        FriendListFragment frag = new FriendListFragment();
//        Bundle args = new Bundle();
//        args.putInt("title", title);
//        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_friend_list_layout,  null);
        friendsRecyclerView = (RecyclerView) view.findViewById(R.id.friend_list);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        friendAdapter = new FriendAdapter(friends);

        Player player = new Player(1l, "id","gkatiforis", "George Katiforis", 100, 1);
        friends.add(player);
        friends.add(player);

        friendsRecyclerView.smoothScrollToPosition(0);
        friendAdapter.notifyDataSetChanged();
        friendsRecyclerView.setAdapter(friendAdapter);
        return view;
    }
}
package com.nooblabs.messaging.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.replaceFragment(container: Int, fragment: Fragment) {
    this.beginTransaction().apply {
        replace(container, fragment)
        commit()
    }
}

fun FragmentManager.replaceFragmentBackStack(container: Int, fragment: Fragment, tag: String) {
    this.beginTransaction().apply {
        replace(container, fragment)
        addToBackStack(tag)
        commit()
    }
}

fun FragmentManager.addFragmentToBackStack(container: Int, fragment: Fragment, tag: String) {
    beginTransaction().apply {
        add(container, fragment)
        addToBackStack(tag)
        commit()
    }
}
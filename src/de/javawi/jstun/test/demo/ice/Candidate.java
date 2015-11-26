/*
 * This file is part of JSTUN. 
 * 
 * Copyright (c) 2005 Thomas King <king@t-king.de> - All rights
 * reserved.
 * 
 * This software is licensed under either the GNU Public License (GPL),
 * or the Apache 2.0 license. Copies of both license agreements are
 * included in this distribution.
 */

package de.javawi.jstun.test.demo.ice;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import de.javawi.jstun.util.Address;
import de.javawi.jstun.util.UtilityException;

public class Candidate implements Comparable<Candidate> {
	// The ieft-mmusic-ice-12 draft is not non-ambigious about the number of
	// types.
	// Chapter 5.1 defines 3 and 4 types on page 16 and page 17, respectively.
	public enum CandidateType {
		Local, ServerReflexive, PeerReflexive, Relayed
	};

	private final DatagramSocket socket;
	private final CandidateType type;
	private short componentId;
	private int priority;
	private int foundationId;
	private Candidate base;
	private boolean isInUse;

	public Candidate(Address address, CandidateType type, short componentId,
			Candidate base) throws SocketException, UnknownHostException,
			UtilityException {
		socket = new DatagramSocket(0, address.getInetAddress());
		this.type = type;
		setComponentId(componentId);
		priority = 0;
		this.base = base;
		isInUse = false;
	}

	public Candidate(Address address, short componentId)
			throws SocketException, UnknownHostException, UtilityException {
		socket = new DatagramSocket(0, address.getInetAddress());
		type = CandidateType.Local;
		this.componentId = componentId;
		priority = 0;
		base = this;
		isInUse = false;
	}

	@Override
	public int compareTo(Candidate cand) {
		return cand.getPriority() - getPriority();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if ((((Candidate) o).socket.equals(socket))
				&& (((Candidate) o).base.equals(base))) {
			return true;
		}
		return false;
	}

	public Address getAddress() throws UtilityException {
		return new Address(socket.getLocalAddress().getAddress());
	}

	public Candidate getBase() {
		return base;
	}

	public CandidateType getCandidateType() {
		return type;
	}

	public short getComponentId() {
		return componentId;
	}

	public int getFoundationId() {
		return foundationId;
	}

	public boolean getInUse() {
		return isInUse;
	}

	public int getPort() {
		return socket.getLocalPort();
	}

	public int getPriority() {
		return priority;
	}

	public void setBase(Candidate base) {
		this.base = base;
	}

	public void setComponentId(short componentId) {
		if ((componentId < 1) || (componentId > 256)) {
			throw new IllegalArgumentException(componentId
					+ " is not between 1 and 256 inclusive.");
		}
		this.componentId = componentId;
	}

	public void setFoundationId(int foundationId) {
		this.foundationId = foundationId;
	}

	public void setInUse(boolean isInUse) {
		this.isInUse = isInUse;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}

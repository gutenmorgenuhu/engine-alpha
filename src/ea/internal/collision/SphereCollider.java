/*
 * Engine Alpha ist eine anfängerorientierte 2D-Gaming Engine.
 *
 * Copyright (c) 2011 - 2014 Michael Andonie and contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package ea.internal.collision;

import ea.Kreis;
import ea.Punkt;
import ea.Vektor;

public class SphereCollider 
extends Collider {
	
	/**
	 * Der Durchmesser des Kreises, das diesen Collider ausmacht.
	 */
	final float durchmesser;
	
	/**
	 * Kreis als Approximation mehrerer Dreiecke für eine effektive CollisionDetection mit
	 * Boxes.
	 */
	Kreis modelsphere;
	
	/**
	 * Erstellt einen neuen sphärischen Collider <b>ohne Offset</b>.
	 * @param durchmesser	Der gewünschte Durchmesser des Colliders.
	 */
	public SphereCollider(float durchmesser) {
		this(durchmesser, Vektor.NULLVEKTOR);
	}

	/**
	 * Erstellt einen neuen sphärischen Collider 
	 * @param durchmesser
	 * @param offset
	 */
	public SphereCollider(float durchmesser, Vektor offset) {
		this(durchmesser, offset, 8);
	}
	
	/**
	 * Erstellt einen neuen sohärischen Collider.
	 * @param durchmesser	Der gewünschte Durchmesser.
	 * @param offset		Der gewünschte Offset.
	 * @param genauigkeit	Die gewünschte Genauigkeit (2^genauigkeit Ecken werden erzeugt für Kollisionstests)
	 */
	public SphereCollider(float durchmesser, Vektor offset, int genauigkeit) { 
		this.offset = offset;
		this.durchmesser = durchmesser;
		modelsphere = new Kreis(0,0, durchmesser, genauigkeit);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean verursachtCollision(Punkt positionThis, Punkt positionOther, Collider collider) {
		if(collider instanceof SphereCollider) {
			return Collider.spheresphereCollision(this, (SphereCollider)collider, positionThis, positionOther);
		} else if(collider instanceof BoxCollider) {
			return Collider.sphereboxCollision(this, (BoxCollider)collider, positionThis, positionOther);
		} else if(collider instanceof ColliderGroup) {
			return collider.verursachtCollision(positionOther, positionThis, this);
		}
		//Default:
		return false;
	}
	
	public Kreis ausDiesem(Punkt position) {
		return new Kreis(position.x + offset.x, position.y + offset.y, modelsphere.radius()*2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean istNullCollider() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collider clone() {
		Collider newSC = modelsphere.erzeugeCollider();
		newSC.offsetSetzen(offset);
		return new SphereCollider(durchmesser, offset);
	}

}

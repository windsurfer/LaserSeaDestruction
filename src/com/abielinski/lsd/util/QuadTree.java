package com.abielinski.lsd.util;

import java.util.ArrayList;

import com.abielinski.lsd.LSDContainer;
import com.abielinski.lsd.LSDG;
import com.abielinski.lsd.LSDSprite;

/**
 * A way of separating  objects for physics calculations
 * @author Adam
 */
public class QuadTree extends Rectangle {
	/**
	 * Flag for specifying that you want to add an object to the A list.
	 */
	static public final int A_LIST = 0;
	/**
	 * Flag for specifying that you want to add an object to the B list.
	 */
	static public final int B_LIST = 1;
	
	/**
	 * The granuality of the quad tree
	 */
	static public int quadTreeDivisions = 3;
	
	
	/**
	 * Whether this branch of the tree can be subdivided or not.
	 */
	protected boolean _canSubdivide;
	
	/**
	 * These variables refer to the internal A and B linked lists,
	 * which are used to store objects in the leaves.
	 */
	protected LSDList _headA;
	protected LSDList _tailA;
	protected LSDList _headB;
	protected LSDList _tailB;
	
	/**
	 * These variables refer to the potential child quadrants for this node.
	 */
	static protected int _min;
	protected QuadTree _nw;
	protected QuadTree _ne;
	protected QuadTree _se;
	protected QuadTree _sw;		
	protected float _l;
	protected float _r;
	protected float _t;
	protected float _b;
	protected float _hw;
	protected float _hh;
	protected float _mx;
	protected float _my;
	
	/**
	 * These objects are used to reduce recursive parameters internally.
	 */
	static protected LSDSprite _o;
	static protected float _ol;
	static protected float _ot;
	static protected float _or;
	static protected float _ob;
	static protected int _oa;
	
	protected QuadTree _parent;
	
	/**
	 * New QuadTree with infinite bounds
	 */
	public QuadTree(){
		super();
		init();
	}
	
	/**
	 * Instantiate a new Quad Tree node.
	 * 
	 * @param	X			The X-coordinate of the point in space.
	 * @param	Y			The Y-coordinate of the point in space.
	 * @param	Width		Desired w of this node.
	 * @param	Height		Desired h of this node.
	 * @param	Parent		The parent branch or node.  Pass null to create a root.
	 */
	public QuadTree(float X, float Y, float Width, float Height, QuadTree Parent)
	{
		super(X-Width/2.0f,Y-Height/2.0f,Width,Height);
		_parent = Parent;
		init();
	}
	
	protected void init(){
		_headA = _tailA = new LSDList();
		_headB = _tailB = new LSDList();
		
		
		//Copy the parent's children (if there are any)
		if(_parent != null)
		{
			LSDList itr;
			LSDList ot;
			if(_parent._headA.object != null)
			{
				itr = _parent._headA;
				while(itr != null)
				{
					if(_tailA.object != null)
					{
						ot = _tailA;
						_tailA = new LSDList();
						ot.next = _tailA;
					}
					_tailA.object = itr.object;
					itr = itr.next;
				}
			}
			if(_parent._headB.object != null)
			{
				itr = _parent._headB;
				while(itr != null)
				{
					if(_tailB.object != null)
					{
						ot = _tailB;
						_tailB = new LSDList();
						ot.next = _tailB;
					}
					_tailB.object = itr.object;
					itr = itr.next;
				}
			}
		}
		else
			_min = (int) ((w + h)/(2*quadTreeDivisions));
		_canSubdivide = (w > _min) || (h > _min);
		
		//Set up comparison/sort helpers
		_nw = null;
		_ne = null;
		_se = null;
		_sw = null;
		_l = pos.x;
		_r = pos.x+w;
		_hw = w/2;
		_mx = _l+_hw;
		_t = pos.y;
		_b = pos.y+h;
		_hh = h/2;
		_my = _t+_hh;
	}
	
	/**
	 * Call this to add an object to the root of the tree.
	 * This will recursively add all group members, but
	 * not the groups themselves.
	 * 
	 * @param	Obj		The <code>LSDSprite</code> you want to add.  <code>FlxGroup</code> objects will be recursed and their applicable members added automatically.
	 * @param	List		A <code>int</code> flag indicating the list to which you want to add the objects.  Options are <code>A_LIST</code> and <code>B_LIST</code>.
	 */
	public void add(LSDSprite Obj, int List) 
	{
		_oa = List;
		if(Obj instanceof LSDContainer)
		{
			ArrayList<LSDSprite> members = ((LSDContainer)Obj).children;
			for(LSDSprite m : members){
				if((m != null))// TODO: figure out if exists is needed && m.exists)
				{
					if(m instanceof LSDContainer){
						add(m,List);
					}else if(m.solid)
					{
						_o = m;
						_ol = _o.pos.x - _o.w/2.0f;
						_ot = _o.pos.y - _o.h/2.0f;
						_or = _o.pos.x + _o.w/2.0f;
						_ob = _o.pos.y + _o.h/2.0f;
						addObject();
					}
				}
			}
			
			
		}else
		if(Obj.solid)
		{
			_o = Obj;
			_ol = _o.pos.x - _o.w/2.0f;
			_ot = _o.pos.y - _o.h/2.0f;
			_or = _o.pos.x + _o.w/2.0f;
			_ob = _o.pos.y + _o.h/2.0f;
			addObject();
		}
	}
	
	/**
	 * Internal for recursively navigating and creating the tree
	 * while adding objects to the appropriate nodes.
	 */
	protected void addObject() 
	{
		//If this quad (not its children) lies entirely inside this object, add it here
		if(!_canSubdivide || ((_l >= _ol) && (_r <= _or) && (_t >= _ot) && (_b <= _ob)))
		{
			addToList();
			return;
		}
		
		//See if the selected object fits completely inside any of the quadrants
		if((_ol > _l) && (_or < _mx))
		{
			if((_ot > _t) && (_ob < _my))
			{
				if(_nw == null)
					_nw = new QuadTree(_l,_t,_hw,_hh,this);
				_nw.addObject();
				return;
			}
			if((_ot > _my) && (_ob < _b))
			{
				if(_sw == null)
					_sw = new QuadTree(_l,_my,_hw,_hh,this);
				_sw.addObject();
				return;
			}
		}
		if((_ol > _mx) && (_or < _r))
		{
			if((_ot > _t) && (_ob < _my))
			{
				if(_ne == null)
					_ne = new QuadTree(_mx,_t,_hw,_hh,this);
				_ne.addObject();
				return;
			}
			if((_ot > _my) && (_ob < _b))
			{
				if(_se == null)
					_se = new QuadTree(_mx,_my,_hw,_hh,this);
				_se.addObject();
				return;
			}
		}
		
		//If it wasn't completely contained we have to check out the partial overlaps
		if((_or > _l) && (_ol < _mx) && (_ob > _t) && (_ot < _my))
		{
			if(_nw == null)
				_nw = new QuadTree(_l,_t,_hw,_hh,this);
			_nw.addObject();
		}
		if((_or > _mx) && (_ol < _r) && (_ob > _t) && (_ot < _my))
		{
			if(_ne == null)
				_ne = new QuadTree(_mx,_t,_hw,_hh,this);
			_ne.addObject();
		}
		if((_or > _mx) && (_ol < _r) && (_ob > _my) && (_ot < _b))
		{
			if(_se == null)
				_se = new QuadTree(_mx,_my,_hw,_hh,this);
			_se.addObject();
		}
		if((_or > _l) && (_ol < _mx) && (_ob > _my) && (_ot < _b))
		{
			if(_sw == null)
				_sw = new QuadTree(_l,_my,_hw,_hh,this);
			_sw.addObject();
		}
	}
	
	/**
	 * Internal for recursively adding objects to leaf lists.
	 */
	protected void addToList() 
	{
		LSDList ot;
		if(_oa == A_LIST)
		{
			if(_tailA.object != null)
			{
				ot = _tailA;
				_tailA = new LSDList();
				ot.next = _tailA;
			}
			_tailA.object = _o;
		}
		else
		{
			if(_tailB.object != null)
			{
				ot = _tailB;
				_tailB = new LSDList();
				ot.next = _tailB;
			}
			_tailB.object = _o;
		}
		if(!_canSubdivide)
			return;
		if(_nw != null)
			_nw.addToList();
		if(_ne != null)
			_ne.addToList();
		if(_se != null)
			_se.addToList();
		if(_sw != null)
			_sw.addToList();
	}
	
	/**
	 * <code>QuadTree</code>'s other main function.  Call this after adding objects
	 * using <code>QuadTree.add()</code> to compare the objects that you loaded.
	 * 
	 * @param	BothLists	Whether you are doing an A-B list comparison, or comparing A against itself.
	 * @param yCollision Whether we should be doing a yCollision
	 *
	 * @return	Whether or not any overlaps were found.
	 */
	public Boolean overlap(boolean BothLists, int yCollision) 
	{
		Boolean c = false;
		LSDList itr;
		if(BothLists)
		{
			//An A-B list comparison
			_oa = B_LIST;
			if(_headA.object != null)
			{
				itr = _headA;
				while(itr != null)
				{
					_o = itr.object;
					if( _o.solid && overlapNode(null, yCollision))
						c = true;
					itr = itr.next;
				}
			}
			_oa = A_LIST;
			if(_headB.object != null)
			{
				itr = _headB;
				while(itr != null)
				{
					_o = itr.object;
					if( _o.solid)
					{
						if((_nw != null) && _nw.overlapNode(null,yCollision))
							c = true;
						if((_ne != null) && _ne.overlapNode(null,yCollision))
							c = true;
						if((_se != null) && _se.overlapNode(null,yCollision))
							c = true;
						if((_sw != null) && _sw.overlapNode(null,yCollision))
							c = true;
					}
					itr = itr.next;
				}
			}
		}
		else
		{
			//Just checking the A list against itself
			if(_headA.object != null)
			{
				itr = _headA;
				while(itr != null)
				{
					_o = itr.object;
					if(_o.solid && overlapNode(itr.next,yCollision))
						c = true;
					itr = itr.next;
				}
			}
		}
		
		//Advance through the tree by calling overlap on each child
		if((_nw != null) && _nw.overlap(BothLists,yCollision))
			c = true;
		if((_ne != null) && _ne.overlap(BothLists,yCollision))
			c = true;
		if((_se != null) && _se.overlap(BothLists,yCollision))
			c = true;
		if((_sw != null) && _sw.overlap(BothLists,yCollision))
			c = true;
		
		return c;
	}
	
	/**
	 * An internal for comparing an object against the contents of a node.
	 * 
	 * @param	Iterator	An optional pointer to a linked list entry (for comparing A against itself).
	 * 
	 * @return	Whether or not any overlaps were found.
	 */
	protected Boolean overlapNode(LSDList Iterator,int yCollision) 
	{
		//member list setup
		Boolean c = false;
		LSDSprite co;
		LSDList itr = Iterator;
		if(itr == null)
		{
			if(_oa == A_LIST)
				itr = _headA;
			else
				itr = _headB;
		}
		
		//Make sure this is a valid list to walk first!
		if(itr.object != null)
		{
			//Walk the list and check for overlaps
			while(itr != null)
			{
				co = itr.object;
				
				if(LSDG.showHulls && LSDG.debug){
					LSDG.theParent.pushMatrix();
						//LSDG.theParent.rectMode(PApplet.CENTER);
						LSDG.theParent.fill(60,255,60,160);
						LSDG.theParent.rect(_o.pos.x-_o.w/2.0f, _o.pos.y-_o.h/2.0f, _o.w, _o.h);
					LSDG.theParent.popMatrix();
					LSDG.theParent.pushMatrix();
						//LSDG.theParent.rectMode(PApplet.CENTER);
						LSDG.theParent.fill(60,255,255,160);
						LSDG.theParent.rect(co.pos.x-co.w/2.0f, co.pos.y-co.h/2.0f, co.w, co.h);
					LSDG.theParent.popMatrix();
				}
				
//				if( (_o == co) ||  !co.solid || !_o.solid ||
//						(_o.pos.x + _o.w  < co.pos.x + LSDG.roundingError) ||
//						(_o.pos.x + LSDG.roundingError > co.pos.x + co.w) ||
//						(_o.pos.y + _o.h < co.pos.y + LSDG.roundingError) ||
//						(_o.pos.y + LSDG.roundingError > co.pos.y + co.h) )
//				{
// 				old flixel collisions...
				if( (_o == co) ||  !co.solid || !_o.solid ||
						(_o.pos.x + _o.w/2.0f  < co.pos.x - co.w/2.0f + LSDG.roundingError) ||
						(_o.pos.x - _o.w/2.0f + LSDG.roundingError > co.pos.x + co.w/2.0f) ||
						(_o.pos.y + _o.h/2.0f  < co.pos.y - co.h/2.0f + LSDG.roundingError) ||
						(_o.pos.y - _o.h/2.0f + LSDG.roundingError > co.pos.y + co.h/2.0f) )
				{
					itr = itr.next;
					continue;
				}
				co.collide(co, _o);
				_o.collide(_o, co);
				if (yCollision == 0){
					if(LSDG.solveYCollision(_o,co))
						c = true;
				}else if (yCollision == 1){
					if(LSDG.solveXCollision(_o,co))
						c = true;
				}
				itr = itr.next;
			}
		}
		
		return c;
	}
	
}

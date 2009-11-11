class Base

  Info=Struct.new(:parent,:filename,:line,:compiler)

  def initialize(info)
    @info=info
    assert{info.is_a?(Info)}
  end
  
  def compiler
    @info.compiler
  end
  def parent
    @info.parent
  end
  def filename
    @info.filename
  end
  def line
    @info.line
  end

  def compile(what)
    info=@info.dup
    info.parent=self
    compiler.compile(info,what,filename)
  end
end

class BlockCode<Base
  def initialize(c,*statements)
    super(c)
    @statements=statements.map{|s|compile(s)}
    @statements.each{|s|
      assert{s.is_a?(Base)}
    }
  end
end

class IfCode<Base
  def initialize(c,cond,thenblock,elseblock)
    super(c)
    @cond=compile(cond)
    @then=compile(thenblock)
    @else=compile(elseblock)
  end
end

class DefinedCode<Base
  def initialize(c,*args)
    super(c)
    @what=compile(*args)
  end
end

class ConstCode<Base
  def initialize(c,name)
    super(c)
    @name=name
    assert{@name.is_a?(Symbol)}
  end
end

class CdeclCode<Base
  def initialize(c,name,rval)
    super(c)
    @name=name
    assert{@name.is_a?(Symbol)}
    @rval=compile(rval)
  end
end

class ModuleCode<Base
  def initialize(c,name,body)
    super(c)
    @name=name
    assert{name.is_a?(Symbol)}
    @body=compile(body)
  end
end

class ClassCode<Base
  def initialize(c,name,parent,body)
    super(c)
    @name=name
    assert{name.is_a?(Symbol)}
    @parent=compile(parent)

    assert{@parent.nil? || @parent.is_a?(ConstCode)}
    @body=compile(body)
  end
end

class ScopeCode<Base
  def initialize(c,*args)
    super(c)
    @body=compile(*args) if args.length>0
    body=@body
    assert{body.nil? || body.is_a?(ClassCode) || body.is_a?(DefsCode) || body.is_a?(BlockCode)}
  end
end

class Colon3Code<Base
  def initialize(c,args)
    super(c)
    @name=args
  end
end

# WHATS' this ?
class Colon2Code<Base
  def initialize(c,parent,name)
    super(c)
    @above=compile(parent)
    @name=name
    assert{@name.is_a?(Symbol)}
  end
end

class FalseCode<Base
  def initialize(c,*args)
    super(c)
  end
end

class TrueCode<Base
  def initialize(c,*args)
    super(c)
  end
end

class NilCode<Base
  def initialize(c,*args)
    super(c)
  end
end

class StrCode<Base
  def initialize(c,str)
    super(c)
    @str=str
  end
end

class CallCode<Base
  def initialize(c,left,name,args)
    super(c)
    @left=compile(left)
    @name=name
    @args=compile(args)

    assert{@name.is_a?(Symbol)}
  end
end

# dynamic string
class DstrCode<Base
  def initialize(c,*es)
    super(c)
    @es=es.map{|e|
      if e.is_a?(String)
        e
      else
        compile(e)
      end
    }
  end
end

class EvstrCode<Base
  def initialize(c,content)
    super(c)
    @content=compile(content)
  end
end

class SelfCode<Base
  def initialize(c)
    super(c)
  end
end

class RescueCode<Base
  def initialize(c,block,rescueBlock)
    super(c)
    @block=compile(block)
    @rescueBlock=compile(rescueBlock)
    assert{not @block.nil?}
    assert{@rescueBlock.is_a?(ResbodyCode)}
  end
end

class DefsCode<Base
  def initialize(c,klass,name,args,body)
    super(c)
    @klass=compile(klass)
    assert{@klass.is_a?(ConstCode) || @klass.is_a?(SelfCode)}
    @name=name
    assert{@name.is_a?(Symbol)}
    @args=compile(args)
    assert{@args.is_a?(ArgsCode)}
    @body=compile(body)
    assert{@body.is_a?(ScopeCode)}
  end
end

class DefnCode<Base
  def initialize(c,name,args,body)
    super(c)
    @name=name
    assert{@name.is_a?(Symbol)}
    @args=compile(args)
    assert{@args.is_a?(ArgsCode)}
    @body=compile(body)
    assert{@body.is_a?(ScopeCode)}
  end
end

class ArgsCode<Base
  def initialize(c,*args)
    super(c)
    @args=args
    args.each{|a|assert{a.is_a?(Symbol)}}
  end
end

class ResbodyCode<Base
  def initialize(c,types,body)
    super(c)
    @types=compile(types)
    assert{@types.is_a?(ArrayCode)}
    @body=compile(body)
    assert{@body.nil? or @body.is_a?(BlockCode)}
  end
end

class ArrayCode<Base
  def initialize(c,*args)
    super(c)
    @elements=args.map{|e|compile(e)}
  end
end

ArglistCode=ArrayCode

class LitCode<Base
  def initialize(c,lit)
    super(c)
    @lit=lit
    assert{lit.is_a?(Symbol) || lit.is_a?(Numeric) || lit.is_a?(Range)}
  end
end

class IasgnCode<Base
  def initialize(c,name,value=nil)
    super(c)
    @name=name
    assert{@name.is_a?(Symbol)}
    @value=compile(value)
  end

  def value=(v)
    @value=v
  end
end

# @args is set, when decompressing
class SplatCode<Base
  def initialize(c,args=nil)
    @args=args
  end
end

LasgnCode=IasgnCode

class GasgnCode<Base
  def initialize(c,name,value)
    super(c)
    @name=name
    @value=value 
  end
end

class AttrasgnCode<Base
  def initialize(c,name,func,attributes)
    super(c)
    @name=compile(name)
    assert{@name.is_a?(LvarCode)}
    @func=func
    assert{@func.is_a?(Symbol)}
    @attributes=compile(attributes)
    assert{@attributes.is_a?(ArglistCode)}
  end
end

# FIXME: dont know
class IterCode<Base
  def initialize(c,collection,iterator,block)
    super(c)
    @collection=compile(collection)
    @iterator=compile(iterator)
    @block=compile(block)
  end
end

class WhileCode<Base
  def initialize(c,block,dontknow,cond)
    super(c)
    @block=compile(block)
    @dontknow=compile(dontknow)
    @cond=cond
    assert{[true].member?(@cond)}
  end
end

class UntilCode<Base
  def initialize(c,block,dontknow,cond)
    super(c)
    @block=compile(block)
    @dontknow=compile(dontknow)
    @cond=cond
    assert{[true].member?(@cond)}
  end
end

class OrCode<Base
  def initialize(c,a,b)
    super(c)
    @a=compile(a)
    @b=compile(b)
  end
end

class AndCode<Base
  def initialize(c,a,b)
    super(c)
    @a=compile(a)
    @b=compile(b)
  end
end

class NotCode<Base
  def initialize(c,a)
    super(c)
    @a=compile(a)
  end
end

class CaseCode<Base
  def initialize(c,var,*a)
    super(c)
    @var=compile(var)
    @as=a.map{|x|compile(x)}
  end
end

class WhenCode<Base
  def initialize(c,cond,block)
    super(c)
    @cond=compile(cond)
    @block=compile(block)
  end
end

class ReturnCode<Base
  def initialize(c,what)
    super(c)
    @what=compile(what)
  end
end

class BreakCode<Base
  def initialize(c)
    super(c)
  end
end

# seems to be block-call parameters
class MasgnCode<Base
  def initialize(c,names,rval=nil)
    super(c)
    @names=compile(names)
    assert{@names.is_a?(ArrayCode)}
    @rval=compile(rval)
    assert{@rval.is_a?(ArrayCode) || @rval.is_a?(To_aryCode) || @rval.nil?}
  end
end

class To_aryCode<Base
  def initialize(c,content)
    super(c)
    @content=c
  end
end

# @xy
class IvarCode<Base
  def initialize(c,name)
    super(c)
    @name=name
    assert{@name.is_a?(Symbol)}
  end
end

# $cx
GvarCode=IvarCode
LvarCode=IvarCode

# a ||= xy
class Op_asgn_orCode<Base
  def initialize(c,left,right)
    super(c)
    @left=compile(left)
    assert{[IvarCode,GvarCode].member?(@left.class)}
    @right=compile(right)
  end
end


class YieldCode<Base
  def initialize(c,*args)
    super(c)
    @args=args.map{|a|compile(a)}
  end
end

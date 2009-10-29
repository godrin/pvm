Code=Struct.new(:before,:within,:after,:value)

class Code
  def combined
    before+within+after
  end

  def to_s
    #pp caller
    [before,within,after].flatten.join("")
  end

  def to_a
    ([before]+[within]+[after]).flatten
  end

  def indent
    self.before=self.before.map{|a|"  "+a}
    self.within=self.within.map{|a|"  "+a}
    self.after=self.after.map{|a|"  "+a}
    self
  end
end

class ASTCode
  attr_accessor :compiler, :parent
  def initialize
    @module=nil
  end

  def currentModule
    @module||(
    @parent.currentModule if @parent
    )||nil
  end

  def s(where,*args)
    args.flatten!

    args=args.map{|a|
      if a.is_a?(Code)
        [a.before,a.within,a.after]
      else
        a
      end
    }
    args.flatten!

    raise "emty !! " if args==["\n"]

    args=args.map{|a|
      if a==:ass
        "="
      elsif a.is_a?(Symbol)
        if a.to_s[0..0]=='_'
          @vars[a]||=tmpVar
        else
          a
        end
      elsif a.is_a?(String)
        a
      elsif a.is_a?(ASTCode)

      elsif a.respond_to?(:getCode)
        a.getCode
      else
        raise "#{args.inspect} is not ok"
      end
    }
    @code[where]||=[]
    @code[where]<<args.join(" ")
  end

  def v(*args)
    s(:value,*args)
  end

  def b(*args)
    s(:before,*(args+["\n"]))
  end

  def w(*args)
    s(:within,*(args+["\n"]))
  end

  def as(*args)
    @code[:after]+=args.flatten.map{|a|a.to_a}.flatten
  end

  def ws(*args)
    @code[:within]+=args.flatten.map{|a|a.to_a}.flatten
  end

  def bs(*args)
    @code[:before]+=args.flatten.map{|a|a.to_a}.flatten
  end

  def a(*args)
    s(:after,*(args+["\n"]))
  end

  def compile(*a)
    @compiler.compile(self,*a)
  end

  def tmpVar
    @compiler.tmpVar
  end

  def append(code)
    assert{code.is_a?(Code)}
    [:before,:within,:after].each{|c|
      @code[c]||=[]
      @code[c]+=code[c]
    }
  end

  def getCode
    @vars={}
    @code={:before=>[],
      :within=>[],
      :after=>[]}

    c=code

    Code.new(@code[:before],@code[:within],@code[:after],@code[:value])
  end
end

class IfCode < ASTCode
  def initialize(cond,pthen,pelse)
    @cond=cond
    @then=pthen
    @else=pelse
  end

  def code
    b :_then, :ass , @then if @then
    b :_else, :ass , @else if @else
    w :_v,:ass , @cond
    w :if, :_v, :then, :_then if @then
    w :unless, :_v, :then, :_else if @else
    a :clear, :_v
    a :clear, :_then if @then
    a :clear, :_else if @else
  end
end

class BlockCode < ASTCode
  def initialize(*argss)
    @argss=argss
  end

  def code
    result=nil
    @argss.map{|as|
      all=[compile(as)].flatten.map{|a|a.indent}.flatten
      ws all
      result=all[-1].after if all[-1] and not all[-1].after=~/ *clear .*/
    }
    v result
  end
end

class SCode < ASTCode
  def initialize(*argss)
    @argss=argss
  end

  def code
    w compile(*@argss)
  end
end

class DefnCode< ASTCode
  def initialize(name,args,body)
    @name=name
    @args=args
    @body=body

  end

  def code
    w(:_func, :ass, "begin", "(",*(compile(@args)+[")"]))
    # pp "BOOODY",@body
    compile(@body).combined.each{|s|
      #  pp "COMBINDES",s
      ws *s
    }

    w "end"
    w "self."+@name.to_s, :ass, :_func
    w "clear",:_func
  end
end

class ScopeCode < ASTCode
  def initialize(*args)
    @args=args
  end

  def code

    ws compile(*@args) if @args.length>0
  end
end

class StrCode < ASTCode
  def initialize(str)
    @str=str
  end

  def code
    b :_tmp,:ass,"\""+@str.gsub("\\","\\\\").gsub("\"","\\")+"\""
    v :_tmp
  end
end

class CallCode<ASTCode
  def initialize(*args)
    @args=args
  end

  def code
    argss=@args
    l=compile(argss[0])
    func=argss[1].to_s
    res2=compile(argss[2])

    args=res2.within
    before=res2.before
    after=res2.after
    pp "L:",l
    if l
      after=after+l.after
      bs l.before
      bs res2.before
      w :_tmp,:ass,l.value,".",func,"(",args,")"
      v :_tmp
      as after
      a :clear,:_tmp
    else
      bs before #if before.length>0
      w :_tmp,:ass,func,"(",args,")"
      v :_tmp
      as after
      a :clear,:_tmp
    end
  end
end

class ConstCode<ASTCode
  def initialize(name)
    @name=name
  end
  def code
    v @name
  end
end

class LvarCode<ASTCode
  def initialize(name)
    @name=name
  end

  def code
    ws @name.to_s
  end
end

class ArgsCode<ASTCode
  def initialize(*args)
    @args=args
  end

  def getCode
    @args.map{|a|[a.to_s,',']}.flatten[0...-1]
  end
end

class LitCode<ASTCode
  def initialize(lit)
    @lit=lit
  end

  def code
    ws @lit.to_s
  end
end

class ArglistCode<ASTCode
  def initialize(*args)
    @args=args
  end

  def code
    first=true
    @args.each{|a|
      t=tmpVar
      res=compile(a)
      b res.before,t,"=",res.within
      if first
        first=false
      else
        ws ','
      end
      ws t

      a res.after if res.after.length>0
      a "clear",t
    }
  end
end

class ModuleCode<ASTCode
  def initialize(name,body)

    assert{not name.nil?}
    #pp "NAME:",name
    #exit
    @name="MODULE_#{name}"
    @module=name
    @body=body

  end

  def code
    b @name,:ass,"VMModule.newModule()"
    ws compile(@body)
  end
end

class ClassCode<ASTCode
  def initialize(name,parent,body)
    #puts "ARGSSSSS:"
    #puts parent.class,name.class,body.class
    assert{not name.nil?}

    @name=name.to_s
    @module=name
    @body=body

  end

  def code
    b @name.to_s,"=","VMKlass.newModule()"
    ws compile(@body)
  end
end

# const declare
class CdeclCode<ASTCode
  def initialize(name,body)
    @name=name.to_s
    @body=body
  end

  def code
    t=tmpVar
    bc=compile(@body)
    bs bc.before,bc.within
    ws t,"=",bc.value
    as bc.after
  end

end

class RescueCode<ASTCode
  def initialize(block,rescueBlock)
    @block=block
    @rescueBlock=rescueBlock
    #puts "---"
    #puts @name
    #puts @block
    #exit
  end
  def code
    rb=compile(@rescueBlock)
    bs rb.before
    w "self.except",:ass,rb.value
    ws compile(@rescueBlock)
    as rb.after
    #FIXME
  end
end

class ResbodyCode<ASTCode
 def initialize(names,body)
   puts ":::",names,"---",body
   @body=body
   @names=names
   #exit
 end
 def code
   names=compile(@names)
   pp "BODY:",@body
   b=compile(@body)
   ws b.before
   w :_block,:ass,b.value
   names.value[1..-2].each{|n|
     w "rescue",n,:_block
   }
   ws b.after
   #pp names
   #exit
 end
end

class ArrayCode<ASTCode
  def initialize(*args)
    @args=args
  end
  def code
    v "["
    @args.each{|arg|
      c=compile(arg)
      bs c.before
      as c.after
      v c.value
    }
    v "]"
  end
end
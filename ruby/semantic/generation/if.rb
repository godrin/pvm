class IfCode
  class LocalBlock
    def initialize(p,s)
      assert{s}
      @parent=p
      @statement=s

      @blockName=tmpVar
    end

    def tmpVar
      @parent.tmpVar
    end

    def before
      c=@statement.before+
      @statement.code+
      @statement.after

      @parent.bgn(@blockName,["*"],c,@statement.value)
    end

    def value
      @blockName
    end

    def after
      s("clear #{@blockName}")
    end
    def s(str)
      @parent.s(str)
    end
  end

  def init
    @tmpVar=tmpVar
  end

  def before
    []
  end

  def code

    bcond=@cond
    bthen=@then
    belse=@else

    bthen=LocalBlock.new(self,bthen) if bthen
    belse=LocalBlock.new(self,belse) if belse

    #pp @else
    #exit

    before=bcond.before
    before+=bthen.before if bthen
    before+=belse.before if belse

    after=bcond.after
    after+=bthen.after if bthen
    after+=belse.after if belse

    val=bcond.value

    within=[]

    within+=s("if #{val} then #{@tmpVar}=#{bthen.value}()") if @then
    within+=s("unless #{val} then #{@tmpVar}=#{belse.value}()") if @else

    before+within+after
  end

  def value
    @tmpVar
  end

  def after
    s("clear #{@tmpVar}")
  end
end
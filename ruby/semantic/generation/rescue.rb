class RescueCode
  def init
    #@blockName=tmpVar
    #@rescueName=tmpVar
    @ret=tmpVar
  end

  def before
    []
  end

  def after
    s("clear #{@ret}")
    #s("clear #{@blockName}")+
    #s("clear #{@rescueName}")
  end

  def code
    s("try")+
    (
    @block.code+
    s("#{@ret}=#{@block.value}")).indent+
    @rescueBlocks.map{|b|

      b.code+
      s("#{@ret}=#{b.value}").indent+
      b.after.indent
    }+
    s("end")

  end

  def value
    @ret
  end
end
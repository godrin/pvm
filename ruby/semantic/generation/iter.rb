class IterCode
  def init
    @blockVar=tmpVar
  end
  
  def before
    []
  end

  # implement this way:
  # 1) use @collection as call-target
  # 2) use x as function-parameter
  # 3) push block as function-parameter
  
  def code
    @collection.before+
    s("#{@blockVar}=begin(#{@iterator.value})")+
    (@block.code+
    s("lreturn #{@block.value}")+
    @block.after).indent+
    s("end")+
    @collection.code([@blockVar])+
    s("clear #{@blockVar}")
  end

  def value
    @collection.value
  end

  def after
    @collection.after
  end
end
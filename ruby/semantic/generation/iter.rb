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
    params=""
    params=@parameters.as_parameters if @parameters
    
    @function.before+
    s("#{@blockVar}=begin(#{params})")+
    (@block.code+
    s("lreturn #{@block.value}")+
    @block.after).indent+
    s("end")+
    @function.code([@blockVar])+
    s("clear #{@blockVar}")
  end

  def value
    @function.value
  end

  def after
    @function.after
  end
end
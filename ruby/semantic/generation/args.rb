class ArgsCode
  def before
    []
  end
  def after
    []
  end
  def value
    if @args.length==0
      return ""
    else
      @args.join(", ")
    end
  end
end